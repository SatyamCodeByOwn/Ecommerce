package app.ecom.controller;

import app.ecom.dto.request_dto.WishlistRequestDTO;
import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.service.WishlistService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService; // Inject your wishlist service

    /**
     * Endpoint to create a new wishlist for a user.
     *
     * @param requestDTO The DTO containing the user ID.
     * @return A ResponseEntity with the created WishlistResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<WishlistResponseDTO> createWishlist(@Valid @RequestBody WishlistRequestDTO requestDTO) {
        WishlistResponseDTO createdWishlist = wishlistService.createWishlist(requestDTO);
        return new ResponseEntity<>(createdWishlist, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get a wishlist by its associated user ID.
     *
     * @param userId The ID of the user whose wishlist is to be retrieved.
     * @return A ResponseEntity containing the WishlistResponseDTO.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<WishlistResponseDTO> getWishlistByUserId(@PathVariable int userId) {
        WishlistResponseDTO wishlist = wishlistService.getWishlistByUserId(userId);
        return ResponseEntity.ok(wishlist);
    }

    /**
     * Endpoint to delete a wishlist by its ID.
     *
     * @param id The ID of the wishlist to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable int id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}
