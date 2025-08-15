package app.ecom.controller;

import app.ecom.dto.request_dto.OrderRequestDTO;
import app.ecom.dto.response_dto.OrderResponseDTO;
import app.ecom.services.OrderService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private  OrderService orderService; // Inject your order service

    /**
     * Endpoint to create a new order.
     *
     * @param orderRequestDTO The DTO containing the order details.
     * @return A ResponseEntity with the created OrderResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = orderService.createOrder(orderRequestDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve an order by its ID.
     *
     * @param id The ID of the order to retrieve.
     * @return A ResponseEntity containing the OrderResponseDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable int id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint to retrieve all orders for a specific user.
     *
     * @param userId The ID of the user.
     * @return A ResponseEntity containing a list of OrderResponseDTOs.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable int userId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint to update the status of an existing order.
     *
     * @param id     The ID of the order to update.
     * @param status The new status for the order.
     * @return A ResponseEntity containing the updated OrderResponseDTO.
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Endpoint to cancel (or delete) an order.
     *
     * @param id The ID of the order to cancel.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable int id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
