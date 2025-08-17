package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CartItemRequestDTO;
import app.ecom.dto.response_dto.CartItemResponseDTO;
import app.ecom.entities.CartItem;
import app.ecom.entities.Product;

public class CartItemMapper {

    public static CartItem toEntity(CartItemRequestDTO dto, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(dto.getQuantity());
        return cartItem;
    }

    public static CartItemResponseDTO toDTO(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }

        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(cartItem.getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setQuantity(cartItem.getQuantity());
        // Assuming your Product entity has a getPrice() method
        dto.setPrice(cartItem.getProduct().getPrice());
        dto.setDateAdded(cartItem.getDateAdded());
        return dto;
    }
}