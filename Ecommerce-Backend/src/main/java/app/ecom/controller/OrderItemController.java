package app.ecom.controller;

import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.services.OrderItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    /**
     * Adds a new item to an existing or new PENDING order for a user.
     *
     * @param userId The ID of the user.
     * @param orderItemRequestDto The DTO containing the product ID and quantity.
     * @return A ResponseEntity with the created OrderItemResponseDto and HTTP status 201 (Created).
     */
    @PostMapping("/orders/users/{userId}/items")
    public ResponseEntity<OrderItemResponseDto> addItemToOrder(
            @PathVariable int userId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto createdItem = orderItemService.addItemToOrder(userId, orderItemRequestDto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Retrieves all items associated with a specific order.
     *
     * @param orderId The ID of the order.
     * @return A ResponseEntity containing a list of OrderItemResponseDto.
     */
    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(@PathVariable int orderId) {
        List<OrderItemResponseDto> items = orderItemService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    /**
     * Updates the quantity of a specific item in an order.
     *
     * @param itemId   The ID of the order item to update.
     * @param quantity The new quantity.
     * @return A ResponseEntity containing the updated OrderItemResponseDto.
     */
    @PutMapping("/order-items/{itemId}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemQuantity(
            @PathVariable int itemId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        OrderItemResponseDto updatedItem = orderItemService.updateOrderItemQuantity(itemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Removes a specific item from an order.
     *
     * @param itemId The ID of the order item to remove.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/order-items/{itemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable int itemId) {
        orderItemService.removeOrderItem(itemId);
        return ResponseEntity.noContent().build();
    }
}