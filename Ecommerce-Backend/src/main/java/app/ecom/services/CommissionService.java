package app.ecom.services;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.*;
import app.ecom.exceptions.custom.OrderNotDeliveredException;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.dto.mappers.CommissionMapper;
import app.ecom.repositories.CommissionRepository;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommissionMapper commissionMapper;

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));
    }

    public List<CommissionResponseDTO> getAllCommissions() {
        return commissionRepository.findAll().stream()
                .map(commissionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    public CommissionResponseDTO getCommissionById(int id) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission not found with id: " + id));

        User loggedInUser = getLoggedInUser();

        // Get the order item from the commission
        OrderItem orderItem = commission.getOrderItem();

        // Get seller of the product in the order item
        User seller = orderItem.getProduct().getSeller();

        // Ownership check for SELLER
        if (loggedInUser.getRole().getName() == Role.RoleName.SELLER && seller.getId() != loggedInUser.getId()) {
            throw new AccessDeniedException("You can only access your own commissions");
        }

        return commissionMapper.toResponseDTO(commission);
    }




  public CommissionResponseDTO createCommission(CommissionRequestDTO requestDTO) {
        // Find the OrderItem first
        OrderItem orderItem = orderItemRepository.findById(requestDTO.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "OrderItem not found with id: " + requestDTO.getOrderItemId()));

        // Check if the order is delivered
        if (orderItem.getOrder().getStatus() != Order.OrderStatus.DELIVERED) {
            throw new OrderNotDeliveredException(orderItem.getOrder().getId());
        }


        // Prevent duplicate commission
        boolean exists = commissionRepository.existsByOrderItemId(orderItem.getId());
        if (exists) {
            throw new ResourceAlreadyExistsException(
                    "Commission", "orderItemId", String.valueOf(orderItem.getId())
            );
        }

        // Perform the commission calculation
        BigDecimal totalSales = BigDecimal.valueOf(orderItem.getPrice())
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        BigDecimal commissionAmount = totalSales.multiply(requestDTO.getCommissionPercentage())
                .divide(BigDecimal.valueOf(100)); // Convert percentage

        // Create the commission entity
        Commission commission = new Commission();
        commission.setOrderItem(orderItem);
        commission.setPlatformFee(requestDTO.getPlatformFee());
        commission.setCommissionPercentage(requestDTO.getCommissionPercentage());
        commission.setCommissionAmount(commissionAmount);


        // Save and return
        Commission savedCommission = commissionRepository.save(commission);
        return commissionMapper.toResponseDTO(savedCommission);
    }




    public void deleteCommission(int id) {
        if (!commissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commission not found with id: " + id);
        }
        commissionRepository.deleteById(id);
    }

   public List<CommissionResponseDTO> getCommissionsByOrderId(int orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems.stream()
                .map(orderItem -> commissionRepository.findByOrderItemId(orderItem.getId())
                        .orElse(null))
                .filter(java.util.Objects::nonNull)
                .map(commissionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }



    public BigDecimal getOwnerRevenue() {
        User loggedInUser = getLoggedInUser();
        Role.RoleName roleName = loggedInUser.getRole().getName();

        if (roleName != Role.RoleName.OWNER) {
            throw new AccessDeniedException("Only OWNER can access total revenue");
        }

        return commissionRepository.findTotalRevenue();
    }


}