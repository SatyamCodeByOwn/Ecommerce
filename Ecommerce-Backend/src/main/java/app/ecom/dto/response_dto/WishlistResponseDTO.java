package app.ecom.dto.response_dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponseDTO {
    private int wishlistId;                       // wishlist id
    private int userId;                            // user id
    private String userName;                       // user name
    private List<WishlistItemResponseDTO> items;   // list of wishlist items
}
