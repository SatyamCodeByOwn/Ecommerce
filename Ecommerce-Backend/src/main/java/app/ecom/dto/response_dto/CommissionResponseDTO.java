package app.ecom.dto.response_dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CommissionResponseDTO {
    private int commissionId;
    private int orderItemId;
    private BigDecimal platformFee;
    private BigDecimal commissionPercentage;
    private BigDecimal commissionAmount;
}