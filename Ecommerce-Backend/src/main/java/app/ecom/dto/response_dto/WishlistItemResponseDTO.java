package app.ecom.dto.response_dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItemResponseDTO {
    private int id;                  // wishlist item id
    private int productId;           // product id
    private String productName;      // product name
    private String productDescription; // product description
    private double productPrice;     // product price
    private LocalDateTime dateAdded; // when added to wishlist
}

