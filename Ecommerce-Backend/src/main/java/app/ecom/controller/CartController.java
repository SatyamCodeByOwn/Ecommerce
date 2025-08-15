package app.ecom.controller;

import app.ecom.dto.request_dto.CartRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.service.CartService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService; // Inject your cart service

    /**
     * Endpoint to create a new cart for a user.
     *
     * @param cartRequestDTO The DTO containing the user ID.
     * @return A ResponseEntity with the created CartResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<CartResponseDTO> createCart(@Valid @RequestBody CartRequestDTO cartRequestDTO) {
        CartResponseDTO createdCart = cartService.createCart(cartRequestDTO);
        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get a cart by its associated user ID.
     *
     * @param userId The ID of the user whose cart is to be retrieved.
     * @return A ResponseEntity containing the CartResponseDTO.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponseDTO> getCartByUserId(@PathVariable Integer userId) {
        CartResponseDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Endpoint to delete a cart by its ID.
     *
     * @param cartId The ID of the cart to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable int cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
