package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionRequestDTO {

    @NotNull(message = "Order item ID is required")
    private Integer orderItemId;

    @NotNull(message = "Platform fee is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Platform fee must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Platform fee can have up to 8 digits and 2 decimal places")
    private BigDecimal platformFee;

    @NotNull(message = "Commission percentage is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Commission percentage must be greater than 0")
    @DecimalMax(value = "100.00", message = "Commission percentage cannot exceed 100")
    @Digits(integer = 3, fraction = 2, message = "Commission percentage can have up to 3 digits and 2 decimal places")
    private BigDecimal commissionPercentage;

    @NotNull(message = "Commission amount is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Commission amount must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Commission amount can have up to 8 digits and 2 decimal places")
    private BigDecimal commissionAmount;
}
