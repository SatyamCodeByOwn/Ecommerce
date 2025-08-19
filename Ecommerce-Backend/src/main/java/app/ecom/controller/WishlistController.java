package app.ecom.controller;

import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
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

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> getOrCreateWishlist(@PathVariable int userId) {
        WishlistResponseDTO wishlist = wishlistService.getOrCreateWishlist(userId);
        return ResponseEntity.ok(
                ApiResponse.<WishlistResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Wishlist fetched successfully")
                        .data(wishlist)
                        .build()
        );
    }

    @PostMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> addProductToWishlist(
            @PathVariable int userId,
            @PathVariable int productId) {
        WishlistResponseDTO wishlist = wishlistService.addProductToWishlist(userId, productId);
        return ResponseEntity.ok(
                ApiResponse.<WishlistResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product added to wishlist successfully")
                        .data(wishlist)
                        .build()
        );
    }

    @DeleteMapping("/users/{userId}/products/{productId}")
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> removeProductFromWishlist(
            @PathVariable int userId,
            @PathVariable int productId) {
        WishlistResponseDTO wishlist = wishlistService.removeProductFromWishlist(userId, productId);
        return ResponseEntity.ok(
                ApiResponse.<WishlistResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product removed from wishlist successfully")
                        .data(wishlist)
                        .build()
        );
    }

    @DeleteMapping("/users/{userId}/clear")
    public ResponseEntity<ApiResponse<WishlistResponseDTO>> clearWishlist(@PathVariable int userId) {
        WishlistResponseDTO wishlist = wishlistService.clearWishlist(userId);
        return ResponseEntity.ok(
                ApiResponse.<WishlistResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Wishlist cleared successfully")
                        .data(wishlist)
                        .build()
        );
    }
}
