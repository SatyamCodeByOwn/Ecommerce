package app.ecom.dto.response_dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemResponseDTO {
    private int id;
    private int wishlistId;
    private int productId;
}
