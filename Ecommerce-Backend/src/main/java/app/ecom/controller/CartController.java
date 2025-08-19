package app.ecom.controller;

import app.ecom.dto.request_dto.CartRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // Get or create a user's cart
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getOrCreateCart(@PathVariable int userId) {
        CartResponseDTO cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cart fetched successfully")
                        .data(cart)
                        .build()
        );
    }

    // Add a product to the cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<ApiResponse<CartResponseDTO>> addProductToCart(
            @PathVariable int userId,
            @RequestBody CartRequestDTO request) {
        CartResponseDTO cart = cartService.addProductToCart(userId, request);
        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product added to cart successfully")
                        .data(cart)
                        .build()
        );
    }

    // Update the quantity of a product in the cart
    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCartItemQuantity(
            @PathVariable int userId,
            @PathVariable int productId,
            @RequestParam int quantity) {
        CartResponseDTO cart = cartService.updateCartItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cart item updated successfully")
                        .data(cart)
                        .build()
        );
    }

    // Remove a product from the cart
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> removeProductFromCart(
            @PathVariable int userId,
            @PathVariable int productId) {
        CartResponseDTO cart = cartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product removed from cart successfully")
                        .data(cart)
                        .build()
        );
    }

    // Clear the cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<CartResponseDTO>> clearCart(@PathVariable int userId) {
        CartResponseDTO cart = cartService.clearCart(userId);
        return ResponseEntity.ok(
                ApiResponse.<CartResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Cart cleared successfully")
                        .data(cart)
                        .build()
        );
    }
}
