package app.ecom.services;

import app.ecom.dto.mappers.OrderItemMapper;
import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Product;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.OrderRepository;
import app.ecom.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * Adds a new item to an existing order.
     *
     * @param orderId             The ID of the order.
     * @param orderItemRequestDto The DTO with product and quantity details.
     * @return The DTO of the newly added order item.
     */
    public OrderItemResponseDto addItemToOrder(int orderId, OrderItemRequestDto orderItemRequestDto) {
        // Find the order or throw an exception
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        // Find the product or throw an exception
        Product product = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderItemRequestDto.getProductId()));

        // Map DTO to entity
        OrderItem orderItem = OrderItemMapper.toEntity(orderItemRequestDto, order, product);

        // Save the new item
        OrderItem savedItem = orderItemRepository.save(orderItem);

        // Return the response DTO
        return OrderItemMapper.toResponseDTO(savedItem);
    }

    /**
     * Retrieves all items for a specific order.
     *
     * @param orderId The ID of the order.
     * @return A list of order item DTOs.
     */
    public List<OrderItemResponseDto> getItemsByOrderId(int orderId) {
        // You will need to add 'findByOrder_Id' method to your OrderItemRepository
        List<OrderItem> items = orderItemRepository.findByOrder_Id(orderId);
        return items.stream()
                .map(OrderItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the quantity of an item in an order.
     *
     * @param itemId   The ID of the order item to update.
     * @param quantity The new quantity.
     * @return The DTO of the updated order item.
     */
    public OrderItemResponseDto updateOrderItemQuantity(int itemId, int quantity) {
        // Find the order item or throw an exception
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + itemId));

        // Update the quantity
        orderItem.setQuantity(quantity);

        // Save the updated item
        OrderItem updatedItem = orderItemRepository.save(orderItem);

        return OrderItemMapper.toResponseDTO(updatedItem);
    }

    /**
     * Removes an item from an order.
     *
     * @param itemId The ID of the order item to remove.
     */
    public void removeOrderItem(int itemId) {
        // Check if the item exists before deleting
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + itemId));

        orderItemRepository.delete(orderItem);
    }
}
