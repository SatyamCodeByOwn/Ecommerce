package app.ecom.services;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.Commission;
import app.ecom.entities.OrderItem;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.dto.mappers.CommissionMapper;
import app.ecom.repositories.CommissionRepository;
import app.ecom.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        // Find the OrderItem first, as it's required for the Commission entity
        OrderItem orderItem = orderItemRepository.findById(requestDTO.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + requestDTO.getOrderItemId()));

        // Perform the commission calculation
        // Total sales value for the order item
        BigDecimal totalSales = BigDecimal.valueOf(orderItem.getPrice())
                .multiply(new BigDecimal(orderItem.getQuantity()));

        // Calculate commission amount: (total sales * percentage)
        BigDecimal commissionAmount = totalSales.multiply(requestDTO.getCommissionPercentage())
                .divide(new BigDecimal(100)); // Divide by 100 to convert percentage

        // Set the calculated values and create the entity
        Commission commission = new Commission();
        commission.setOrderItem(orderItem);
        commission.setPlatformFee(requestDTO.getPlatformFee());
        commission.setCommissionPercentage(requestDTO.getCommissionPercentage());
        commission.setCommissionAmount(commissionAmount);

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
}