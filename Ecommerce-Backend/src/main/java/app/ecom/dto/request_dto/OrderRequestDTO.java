package app.ecom.dto.request_dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be a positive number")
    private Integer userId;

    @NotEmpty(message = "Order items list cannot be empty")
    private List<@Valid OrderItemRequestDto> items;

    @NotNull(message = "Shipping address ID is required")
    private Integer shippingAddressId;
}