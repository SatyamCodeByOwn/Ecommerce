package app.ecom.controller;

import app.ecom.dto.request_dto.WishlistItemRequestDTO;
import app.ecom.dto.response_dto.WishlistItemResponseDTO;
import app.ecom.service.WishlistItemService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist-items")
@RequiredArgsConstructor
public class WishlistItemController {

    private final WishlistItemService wishlistItemService; // Inject your service

    /**
     * Endpoint to add a product to a wishlist.
     *
     * @param requestDTO The DTO containing wishlist and product IDs.
     * @return A ResponseEntity with the created WishlistItemResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<WishlistItemResponseDTO> addWishlistItem(@Valid @RequestBody WishlistItemRequestDTO requestDTO) {
        WishlistItemResponseDTO createdItem = wishlistItemService.addWishlistItem(requestDTO);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all items for a specific wishlist.
     *
     * @param wishlistId The ID of the wishlist.
     * @return A ResponseEntity containing a list of WishlistItemResponseDTOs.
     */
    @GetMapping("/wishlist/{wishlistId}")
    public ResponseEntity<List<WishlistItemResponseDTO>> getItemsByWishlistId(@PathVariable int wishlistId) {
        List<WishlistItemResponseDTO> items = wishlistItemService.getItemsByWishlistId(wishlistId);
        return ResponseEntity.ok(items);
    }

    /**
     * Endpoint to remove an item from a wishlist by its ID.
     *
     * @param id The ID of the wishlist item to remove.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeWishlistItem(@PathVariable int id) {
        wishlistItemService.removeWishlistItem(id);
        return ResponseEntity.noContent().build();
    }
}
