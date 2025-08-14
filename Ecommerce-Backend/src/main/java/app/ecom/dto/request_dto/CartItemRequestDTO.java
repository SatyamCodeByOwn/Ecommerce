package app.ecom.dto.request_dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequestDTO {

    @NotNull(message = "Cart ID is required.")
    private Integer cartId;

    @NotNull(message = "Product ID is required.")
    private Integer productId;

    @Min(value = 1, message = "Quantity must be at least 1.")
    private int quantity;
}
