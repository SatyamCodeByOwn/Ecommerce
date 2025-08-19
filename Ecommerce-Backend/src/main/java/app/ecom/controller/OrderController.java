package app.ecom.controller;

import app.ecom.dto.request_dto.OrderRequestDTO;
import app.ecom.dto.response_dto.OrderResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
        return new ResponseEntity<>(
                ApiResponse.<OrderResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Order created successfully")
                        .data(createdOrder)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ (Single)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable int id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order fetched successfully")
                        .data(order)
                        .build()
        );
    }

    // READ (By User)
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> getOrdersByUserId(@PathVariable int userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.<List<OrderResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Orders for user fetched successfully")
                        .data(orders)
                        .build()
        );
    }

    // UPDATE Status
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order status updated successfully")
                        .data(updatedOrder)
                        .build()
        );
    }

    // DELETE (Cancel)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable int id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order cancelled successfully")
                        .data(null)
                        .build()
        );
    }
}
