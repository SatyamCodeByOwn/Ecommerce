package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.entities.Wishlist;
import java.util.stream.Collectors;

public class WishlistMapper {

    public static WishlistResponseDTO toResponseDTO(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        return new WishlistResponseDTO(
                wishlist.getId(),
                wishlist.getUser().getId(),
                wishlist.getWishlistItems().stream()
                        .map(WishlistItemMapper::toResponseDTO)
                        .collect(Collectors.toList())
        );
    }
}