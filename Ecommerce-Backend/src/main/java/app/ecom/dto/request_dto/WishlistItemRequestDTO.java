package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemRequestDTO {

    @Positive(message = "Wishlist ID must be a positive number")
    private int wishlistId;

    @Positive(message = "Product ID must be a positive number")
    private int productId;
}
