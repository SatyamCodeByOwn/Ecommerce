package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.CartItemResponseDTO;
import app.ecom.entities.CartItem;

public class CartItemMapper {

    public static CartItemResponseDTO toResponseDTO(CartItem cartItem) {
        return CartItemResponseDTO.builder()
                .id(cartItem.getId())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .productDescription(cartItem.getProduct().getDescription())
                .productPrice(cartItem.getProduct().getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
