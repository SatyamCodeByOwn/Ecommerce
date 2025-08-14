package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    @NotEmpty(message = "Order items list cannot be empty")
    private List<@NotNull(message = "Order item ID cannot be null") Integer> orderItemIds;

    @NotNull(message = "Order status is required")
    private String status; // Will match one of the enum values in OrderStatus

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than zero")
    private Double totalAmount;

    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    private Integer shippingAddressId;
}
