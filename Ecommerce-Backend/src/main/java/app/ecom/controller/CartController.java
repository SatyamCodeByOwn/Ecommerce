package app.ecom.controller; // Assuming your controllers are in this package

import app.ecom.dto.request_dto.CartItemRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    // GET a user's cart (or create an empty one)
    // GET /api/carts/users/{userId}
    @GetMapping("/users/{userId}")
    public ResponseEntity<CartResponseDTO> getOrCreateCart(@PathVariable int userId) {
        CartResponseDTO cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(cart);
    }

    // ADD a product to a user's cart (or update quantity if exists)
    // POST /api/carts/users/{userId}/items
    @PostMapping("/users/{userId}/items")
    public ResponseEntity<CartResponseDTO> addProductToCart(
            @PathVariable int userId,
            @Valid @RequestBody CartItemRequestDTO dto) {
        CartResponseDTO updatedCart = cartService.addProductToCart(userId, dto);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // UPDATE the quantity of a product in the cart
    // PUT /api/carts/users/{userId}/items/{productId}
    @PutMapping("/users/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> updateCartItemQuantity(
            @PathVariable int userId,
            @PathVariable int productId,
            @RequestParam int quantity) { // Use @RequestParam for quantity
        CartResponseDTO updatedCart = cartService.updateCartItemQuantity(userId, productId, quantity);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // REMOVE a product from a user's cart
    // DELETE /api/carts/users/{userId}/items/{productId}
    @DeleteMapping("/users/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeProductFromCart(
            @PathVariable int userId,
            @PathVariable int productId) {
        CartResponseDTO updatedCart = cartService.removeProductFromCart(userId, productId);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    // CLEAR all items from a user's cart
    // DELETE /api/carts/users/{userId}/clear
    @DeleteMapping("/users/{userId}/clear")
    public ResponseEntity<CartResponseDTO> clearCart(@PathVariable int userId) {
        CartResponseDTO clearedCart = cartService.clearCart(userId);
        return new ResponseEntity<>(clearedCart, HttpStatus.OK);
    }
}