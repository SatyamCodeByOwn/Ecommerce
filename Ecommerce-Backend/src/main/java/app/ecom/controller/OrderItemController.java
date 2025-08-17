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

    @PostMapping("/orders/users/{userId}/items")
    public ResponseEntity<OrderItemResponseDto> addItemToOrder(
            @PathVariable int userId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto createdItem = orderItemService.addItemToOrder(userId, orderItemRequestDto);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDto>> getItemsByOrderId(@PathVariable int orderId) {
        List<OrderItemResponseDto> items = orderItemService.getItemsByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    @PutMapping("/order-items/{itemId}")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemQuantity(
            @PathVariable int itemId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        OrderItemResponseDto updatedItem = orderItemService.updateOrderItemQuantity(itemId, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/order-items/{itemId}")
    public ResponseEntity<Void> removeOrderItem(@PathVariable int itemId) {
        orderItemService.removeOrderItem(itemId);
        return ResponseEntity.noContent().build();
    }
}