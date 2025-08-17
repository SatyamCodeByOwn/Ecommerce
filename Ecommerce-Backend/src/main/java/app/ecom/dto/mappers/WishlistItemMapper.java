package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.WishlistItemResponseDTO;
import app.ecom.entities.WishlistItem;

public class WishlistItemMapper {
    public static WishlistItemResponseDTO toResponseDTO(WishlistItem item) {
        if (item == null || item.getProduct() == null) {
            return null;
        }

        return new WishlistItemResponseDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getProduct().getImagePath(),
                item.getDateAdded()
        );
    }
}