package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CartItemRequestDTO;
import app.ecom.dto.response_dto.CartItemResponseDTO;
import app.ecom.entities.Cart;
import app.ecom.entities.CartItem;
import app.ecom.entities.Product;

public class CartItemMapper {

    // Convert Request DTO -> Entity
    public static CartItem toEntity(CartItemRequestDTO dto, Cart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);       // fetched in service layer
        cartItem.setProduct(product); // fetched in service layer
        cartItem.setQuantity(dto.getQuantity());
        return cartItem;
    }

    // Convert Entity -> Response DTO
    public static CartItemResponseDTO toResponseDTO(CartItem cartItem) {
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setCartItemId(cartItem.getCartItemId());
        dto.setUsername(cartItem.getCart().getUser().getUsername());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
