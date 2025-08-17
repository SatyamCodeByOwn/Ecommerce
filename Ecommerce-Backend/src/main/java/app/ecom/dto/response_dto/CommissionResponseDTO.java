package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionResponseDTO {

    private int id; // Corresponds to commissionId in entity
    private int orderItemId;
    private int orderId; // For easy lookup
    private int productId; // For easy lookup
    private String productName; // For easy lookup
    private BigDecimal platformFee;
    private BigDecimal commissionPercentage;
    private BigDecimal commissionAmount;
}