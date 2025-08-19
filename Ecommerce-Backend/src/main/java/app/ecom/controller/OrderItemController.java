package app.ecom.controller;

import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.exceptions.response_api.ApiResponse;
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

    // CREATE (Add item to user's order)
    @PostMapping("/orders/users/{userId}/items")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> addItemToOrder(
            @PathVariable int userId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto createdItem = orderItemService.addItemToOrder(userId, orderItemRequestDto);
        return new ResponseEntity<>(
                ApiResponse.<OrderItemResponseDto>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Item added to order successfully")
                        .data(createdItem)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ (Items by orderId)
    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<ApiResponse<List<OrderItemResponseDto>>> getItemsByOrderId(@PathVariable int orderId) {
        List<OrderItemResponseDto> items = orderItemService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponse.<List<OrderItemResponseDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Items fetched successfully for the order")
                        .data(items)
                        .build()
        );
    }

    // UPDATE (Quantity of an order item)
    @PutMapping("/order-items/{itemId}")
    public ResponseEntity<ApiResponse<OrderItemResponseDto>> updateOrderItemQuantity(
            @PathVariable int itemId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        OrderItemResponseDto updatedItem = orderItemService.updateOrderItemQuantity(itemId, quantity);
        return ResponseEntity.ok(
                ApiResponse.<OrderItemResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order item quantity updated successfully")
                        .data(updatedItem)
                        .build()
        );
    }

    // DELETE (Remove order item)
    @DeleteMapping("/order-items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> removeOrderItem(@PathVariable int itemId) {
        orderItemService.removeOrderItem(itemId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order item removed successfully")
                        .data(null)
                        .build()
        );
    }
}
