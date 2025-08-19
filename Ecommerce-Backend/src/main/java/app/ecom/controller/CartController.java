package app.ecom.controller;

import app.ecom.dto.request_dto.CartRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ Get or create a user's cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getOrCreateCart(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.getOrCreateCart(userId));
    }

    // ✅ Add a product to the cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @PathVariable int userId,
            @RequestBody CartRequestDTO request) {
        return ResponseEntity.ok(cartService.addProductToCart(userId, request));
    }

    // ✅ Update the quantity of a product in the cart
    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> updateCartItemQuantity(
            @PathVariable int userId,
            @PathVariable int productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(userId, productId, quantity));
    }

    // ✅ Remove a product from the cart
    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeProductFromCart(
            @PathVariable int userId,
            @PathVariable int productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }

    // ✅ Clear the cart
    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<CartResponseDTO> clearCart(@PathVariable int userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }
}
