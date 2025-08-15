//package app.ecom.controller;
//
//import app.ecom.dto.request_dto.CartItemRequestDTO;
//import app.ecom.dto.response_dto.CartItemResponseDTO;
//import app.ecom.service.CartItemService; // You will need to create this service
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/cart-items")
//@RequiredArgsConstructor
//public class CartItemController {
//
//    private final CartItemService cartItemService; // Inject your cart item service
//
//    /**
//     * Endpoint to add a new item to a user's cart.
//     *
//     * @param cartItemRequestDTO The DTO containing cart ID, product ID, and quantity.
//     * @return A ResponseEntity with the created CartItemResponseDTO and HTTP status 201 (Created).
//     */
//    @PostMapping
//    public ResponseEntity<CartItemResponseDTO> addCartItem(@Valid @RequestBody CartItemRequestDTO cartItemRequestDTO) {
//        CartItemResponseDTO createdItem = cartItemService.addCartItem(cartItemRequestDTO);
//        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
//    }
//
//    /**
//     * Endpoint to get all items for a specific cart.
//     *
//     * @param cartId The ID of the cart.
//     * @return A ResponseEntity containing a list of CartItemResponseDTO.
//     */
//    @GetMapping("/cart/{cartId}")
//    public ResponseEntity<List<CartItemResponseDTO>> getCartItemsByCartId(@PathVariable Integer cartId) {
//        List<CartItemResponseDTO> items = cartItemService.getCartItemsByCartId(cartId);
//        return ResponseEntity.ok(items);
//    }
//
//    /**
//     * Endpoint to update the quantity of an existing item in the cart.
//     *
//     * @param cartItemId The ID of the cart item to update.
//     * @param quantity The new quantity.
//     * @return A ResponseEntity containing the updated CartItemResponseDTO.
//     */
//    @PutMapping("/{cartItemId}")
//    public ResponseEntity<CartItemResponseDTO> updateCartItemQuantity(
//            @PathVariable int cartItemId,
//            @RequestParam int quantity) {
//        CartItemResponseDTO updatedItem = cartItemService.updateCartItemQuantity(cartItemId, quantity);
//        return ResponseEntity.ok(updatedItem);
//    }
//
//    /**
//     * Endpoint to remove an item from the cart.
//     *
//     * @param cartItemId The ID of the cart item to remove.
//     * @return A ResponseEntity with HTTP status 204 (No Content).
//     */
//    @DeleteMapping("/{cartItemId}")
//    public ResponseEntity<Void> removeCartItem(@PathVariable int cartItemId) {
//        cartItemService.removeCartItem(cartItemId);
//        return ResponseEntity.noContent().build();
//    }
//}
