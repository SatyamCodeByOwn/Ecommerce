package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.WishlistRequestDTO;
import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.entities.User;
import app.ecom.entities.Wishlist;

public class WishlistMapper {

    // Convert Request DTO → Entity
    public static Wishlist toEntity(WishlistRequestDTO dto, User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlist;
    }

    // Convert Entity → Response DTO
    public static WishlistResponseDTO toDTO(Wishlist wishlist) {
        return new WishlistResponseDTO(
                wishlist.getId(),
                wishlist.getUser().getId()
        );
    }
}
