package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.entities.Cart;
import app.ecom.entities.CartItem;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDTO toDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCartItems(cart.getCartItems().stream()
                .map(CartItemMapper::toDTO)
                .collect(Collectors.toSet()));

        double totalAmount = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        dto.setTotalAmount(totalAmount);

        return dto;
    }
}