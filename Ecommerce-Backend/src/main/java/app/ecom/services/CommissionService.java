package app.ecom.services;

import app.ecom.dto.mappers.CommissionMapper;
import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.Commission;
import app.ecom.entities.OrderItem;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.CommissionRepository;
import app.ecom.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommissionService {

    @Autowired
    private CommissionRepository commissionRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    // Define platform commission rate (e.g., 10%)
    private static final BigDecimal PLATFORM_COMMISSION_PERCENTAGE = new BigDecimal("0.10"); // 10%

    /**
     * Creates a new commission record for a given order item.
     * Calculates platform fee and commission amount.
     *
     * @param dto The DTO containing the order item ID.
     * @return The DTO of the created commission.
     */
    public CommissionResponseDTO createCommission(CommissionRequestDTO dto) {
        OrderItem orderItem = orderItemRepository.findById(dto.getOrderItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id: " + dto.getOrderItemId()));

        // Check if a commission already exists for this order item to prevent duplicates
        Optional<Commission> existingCommission = commissionRepository.findByOrderItemId(orderItem.getId());
        if (existingCommission.isPresent()) {
            throw new IllegalArgumentException("Commission already exists for OrderItem ID: " + orderItem.getId());
        }

        // Calculate platform fee based on order item's total price (quantity * price)
        BigDecimal itemTotalPrice = BigDecimal.valueOf(orderItem.getPrice()).multiply(BigDecimal.valueOf(orderItem.getQuantity()));
        BigDecimal platformFee = itemTotalPrice.multiply(PLATFORM_COMMISSION_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP); // Round to 2 decimal places

        // Assuming commissionPercentage from entity for now, or it could be configurable
        // For a fixed rate, we use PLATFORM_COMMISSION_PERCENTAGE here.
        BigDecimal commissionAmount = platformFee; // In this simple model, commission amount is the platform fee

        Commission commission = CommissionMapper.toEntity(orderItem, platformFee, PLATFORM_COMMISSION_PERCENTAGE.multiply(new BigDecimal("100")), commissionAmount);
        Commission savedCommission = commissionRepository.save(commission);

        return CommissionMapper.toResponseDTO(savedCommission);
    }

    /**
     * Retrieves a commission by its ID.
     *
     * @param id The ID of the commission.
     * @return The DTO of the retrieved commission.
     */
    public CommissionResponseDTO getCommissionById(int id) {
        Commission commission = commissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission not found with id: " + id));
        return CommissionMapper.toResponseDTO(commission);
    }

    /**
     * Retrieves all commissions for a specific order.
     *
     * @param orderId The ID of the order.
     * @return A list of commission DTOs.
     */
    public List<CommissionResponseDTO> getCommissionsByOrderId(int orderId) {
        List<Commission> commissions = commissionRepository.findByOrderItem_Order_Id(orderId);
        return commissions.stream()
                .map(CommissionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}