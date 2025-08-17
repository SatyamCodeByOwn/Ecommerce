package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.WishlistItemResponseDTO;
import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.entities.Wishlist;

import java.util.List;
import java.util.stream.Collectors;

public class WishlistMapper {

    public static WishlistResponseDTO toResponseDTO(Wishlist wishlist) {
        List<WishlistItemResponseDTO> items = wishlist.getWishlistItems().stream()
                .map(item -> {
                    return app.ecom.dto.mappers.WishlistItemMapper.toResponseDTO(item);
                })
                .collect(Collectors.toList());

        return WishlistResponseDTO.builder()
                .wishlistId(wishlist.getId())
                .userId(wishlist.getUser().getId())
                .userName(wishlist.getUser().getUsername()) // adjust based on your User entity
                .items(items)
                .build();
    }
}
