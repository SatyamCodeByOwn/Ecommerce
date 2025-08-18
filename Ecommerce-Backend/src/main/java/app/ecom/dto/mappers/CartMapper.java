package app.ecom.dto.mappers;

import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.dto.response_dto.CartItemResponseDTO;
import app.ecom.entities.Cart;


import java.util.List;

import java.util.stream.Collectors;

public class CartMapper {


    public static CartResponseDTO toResponseDTO(Cart cart) {
        List<CartItemResponseDTO> items = cart.getCartItems().stream()
                .map(CartItemMapper::toResponseDTO) // ✅ delegate to CartItemMapper
                .collect(Collectors.toList());

        double totalPrice = items.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())

                .sum();
        


        return CartResponseDTO.builder()
                .cartId(cart.getId())
                .userId(cart.getUser().getId())
                .userName(cart.getUser().getUsername()) // adjust based on your User entity
                .items(items)
                .totalPrice(totalPrice)
                .build();

       
    }
}
