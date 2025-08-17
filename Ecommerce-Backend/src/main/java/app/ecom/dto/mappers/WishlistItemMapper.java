package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.WishlistItemResponseDTO;
import app.ecom.entities.WishlistItem;

public class WishlistItemMapper {

    public static WishlistItemResponseDTO toResponseDTO(WishlistItem item) {
        return WishlistItemResponseDTO.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productDescription(item.getProduct().getDescription())
                .productPrice(item.getProduct().getPrice())
                .dateAdded(item.getDateAdded())
                .build();
    }
}
