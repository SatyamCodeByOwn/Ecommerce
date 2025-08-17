package app.ecom.controller;

import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    // GET a user's wishlist (or create an empty one)
    // GET /api/wishlist/users/{userId}
    @GetMapping("/users/{userId}")
    public ResponseEntity<WishlistResponseDTO> getOrCreateWishlist(@PathVariable int userId) {
        WishlistResponseDTO wishlist = wishlistService.getOrCreateWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }

    // ADD a product to a user's wishlist
    // POST /api/wishlist/users/{userId}/products/{productId}
    @PostMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<WishlistResponseDTO> addProductToWishlist(
            @PathVariable int userId,
            @PathVariable int productId) {
        WishlistResponseDTO updatedWishlist = wishlistService.addProductToWishlist(userId, productId);
        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
    }

    // REMOVE a product from a user's wishlist
    // DELETE /api/wishlist/users/{userId}/products/{productId}
    @DeleteMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<WishlistResponseDTO> removeProductFromWishlist(
            @PathVariable int userId,
            @PathVariable int productId) {
        WishlistResponseDTO updatedWishlist = wishlistService.removeProductFromWishlist(userId, productId);
        return new ResponseEntity<>(updatedWishlist, HttpStatus.OK);
    }
}