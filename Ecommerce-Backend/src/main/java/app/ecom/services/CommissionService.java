package app.ecom.services;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.Commission;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.exceptions.custom.OrderNotDeliveredException;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.dto.mappers.CommissionMapper;
import app.ecom.repositories.CommissionRepository;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OrderRepository orderRepository;

    @Autowired
    private CommissionMapper commissionMapper;

    public List<CommissionResponseDTO> getAllCommissions() {
        return commissionRepository.findAll().stream()
                .map(commissionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CommissionResponseDTO getCommissionById(int id) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission not found with id: " + id));
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


/*
    @Transactional
    public CommissionResponseDTO createCommissionForDeliveredOrder(OrderItem orderItem, BigDecimal platformFee, BigDecimal commissionPercentage) {
        if (orderItem.getOrder().getStatus() != Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Order not delivered yet; cannot create commission");
        }

        boolean exists = commissionRepository.existsByOrderItemId(orderItem.getId());
        if (exists) return null;

        BigDecimal totalSales = BigDecimal.valueOf(orderItem.getPrice())
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        BigDecimal commissionAmount = totalSales.multiply(commissionPercentage)
                .divide(BigDecimal.valueOf(100));

        Commission commission = new Commission();
        commission.setOrderItem(orderItem);
        commission.setPlatformFee(platformFee);
        commission.setCommissionPercentage(commissionPercentage);
        commission.setCommissionAmount(commissionAmount);


        return commissionMapper.toResponseDTO(commissionRepository.save(commission));
    }*/

    public void deleteCommission(int id) {
        if (!commissionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commission not found with id: " + id);
        }
        commissionRepository.deleteById(id);
    }

    public List<CommissionResponseDTO> getCommissionsByOrderId(int orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems.stream()
                .map(orderItem -> commissionRepository.findByOrderItemId(orderItem.getId())
                        .orElse(null))
                .filter(java.util.Objects::nonNull)
                .map(commissionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    public BigDecimal getOwnerRevenue() {
        return commissionRepository.findTotalRevenue();
    }

}