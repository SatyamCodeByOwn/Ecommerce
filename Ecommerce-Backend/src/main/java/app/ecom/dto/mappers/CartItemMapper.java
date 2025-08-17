package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.CartItemResponseDTO;
import app.ecom.entities.CartItem;

public class CartItemMapper {

    public static CartItemResponseDTO toResponseDTO(CartItem item) {
        if (item == null || item.getProduct() == null) {
            return null;
        }

        return new CartItemResponseDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getQuantity() * item.getProduct().getPrice(),
                item.getProduct().getImagePath(),
                item.getDateAdded()
        );
    }
}