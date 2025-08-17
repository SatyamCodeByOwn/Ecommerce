package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.Commission;
import app.ecom.entities.OrderItem;
import java.math.BigDecimal; // Import BigDecimal

public class CommissionMapper {

    // toEntity is generally less common for Commission if it's auto-generated
    // But if you explicitly create it, it would look like this:
    public static Commission toEntity(OrderItem orderItem, BigDecimal platformFee,
                                      BigDecimal commissionPercentage, BigDecimal commissionAmount) {
        Commission commission = new Commission();
        commission.setOrderItem(orderItem);
        commission.setPlatformFee(platformFee);
        commission.setCommissionPercentage(commissionPercentage);
        commission.setCommissionAmount(commissionAmount);
        return commission;
    }

    public static CommissionResponseDTO toResponseDTO(Commission commission) {
        if (commission == null || commission.getOrderItem() == null || commission.getOrderItem().getProduct() == null) {
            return null; // Handle null relations gracefully
        }
        return new CommissionResponseDTO(
                commission.getCommissionId(),
                commission.getOrderItem().getId(),
                commission.getOrderItem().getOrder().getId(), // Access order ID through order item
                commission.getOrderItem().getProduct().getId(),
                commission.getOrderItem().getProduct().getName(),
                commission.getPlatformFee(),
                commission.getCommissionPercentage(),
                commission.getCommissionAmount()
        );
    }
}