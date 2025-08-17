package app.ecom.dto.request_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommissionRequestDTO {

    @NotNull(message = "Order item ID is required")
    @Positive(message = "Order item ID must be a positive number")
    private Integer orderItemId;

    // We assume platformFee and commissionPercentage are calculated/derived on the backend.
    // If commissionPercentage can be set by the user, add it here with @DecimalMin/@DecimalMax.
}