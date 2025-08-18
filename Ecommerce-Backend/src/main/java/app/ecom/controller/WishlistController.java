package app.ecom.controller;

import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // ✅ Get the wishlist of a user (or create empty one if none exists)
    // GET /api/wishlist/users/{userId}
    @GetMapping("/users/{userId}")
    public ResponseEntity<WishlistResponseDTO> getOrCreateWishlist(@PathVariable int userId) {
        return ResponseEntity.ok(wishlistService.getOrCreateWishlist(userId));
    }

    // ✅ Add a product to the user's wishlist
    // POST /api/wishlist/users/{userId}/products/{productId}
    @PostMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<WishlistResponseDTO> addProductToWishlist(
            @PathVariable int userId,
            @PathVariable int productId) {
        return ResponseEntity.ok(wishlistService.addProductToWishlist(userId, productId));
    }

    // ✅ Remove a product from the user's wishlist
    // DELETE /api/wishlist/users/{userId}/products/{productId}
    @DeleteMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<WishlistResponseDTO> removeProductFromWishlist(
            @PathVariable int userId,
            @PathVariable int productId) {
        return ResponseEntity.ok(wishlistService.removeProductFromWishlist(userId, productId));
    }

    // ✅ Clear the wishlist
    // DELETE /api/wishlist/users/{userId}/clear
    @DeleteMapping("/users/{userId}/clear")
    public ResponseEntity<WishlistResponseDTO> clearWishlist(@PathVariable int userId) {
        return ResponseEntity.ok(wishlistService.clearWishlist(userId));
    }
}
