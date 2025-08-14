package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CartRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.entities.Cart;
import app.ecom.entities.User;

public class CartMapper {

    // Convert Request DTO -> Entity
    public static Cart toEntity(CartRequestDTO dto, User user) {
        Cart cart = new Cart();
        cart.setUser(user); // User fetched from DB in service layer
        return cart;
    }

    // Convert Entity -> Response DTO
    public static CartResponseDTO toResponseDTO(Cart cart) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setCartId(cart.getCartId());
        dto.setUsername(cart.getUser().getUsername());
        return dto;
    }
}
