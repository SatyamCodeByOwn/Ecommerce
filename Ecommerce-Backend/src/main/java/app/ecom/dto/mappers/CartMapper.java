package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.entities.Cart;
import java.util.stream.Collectors;

public class CartMapper {

    public static CartResponseDTO toResponseDTO(Cart cart) {
        if (cart == null) {
            return null;
        }

        double totalCartPrice = cart.getCartItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();

        return new CartResponseDTO(
                cart.getId(),
                cart.getUser().getId(),
                cart.getCartItems().stream()
                        .map(CartItemMapper::toResponseDTO)
                        .collect(Collectors.toList()),
                totalCartPrice
        );
    }
}