package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.WishlistItemRequestDTO;
import app.ecom.dto.response_dto.WishlistItemResponseDTO;
import app.ecom.entities.Product;
import app.ecom.entities.Wishlist;
import app.ecom.entities.WishlistItem;

public class WishlistItemMapper {

    // Convert Request DTO → Entity
    public static WishlistItem toEntity(WishlistItemRequestDTO dto, Wishlist wishlist, Product product) {
        WishlistItem item = new WishlistItem();
        item.setWishlist(wishlist);
        item.setProduct(product);
        return item;
    }

    // Convert Entity → Response DTO
    public static WishlistItemResponseDTO toDTO(WishlistItem item) {
        return new WishlistItemResponseDTO(
                item.getId(),
                item.getWishlist().getId(),
                item.getProduct().getId()
        );
    }
}
