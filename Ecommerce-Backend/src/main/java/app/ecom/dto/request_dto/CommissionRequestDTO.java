package app.ecom.dto.request_dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CommissionRequestDTO {
    private int orderItemId;
    private BigDecimal platformFee;
    private BigDecimal commissionPercentage;
}