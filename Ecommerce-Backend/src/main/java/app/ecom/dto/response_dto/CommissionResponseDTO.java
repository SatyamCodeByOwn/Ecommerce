package app.ecom.dto.response_dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionResponseDTO {

    private int commissionId;
    private int orderItemId;
    private BigDecimal platformFee;
    private BigDecimal commissionPercentage;
    private BigDecimal commissionAmount;
}
