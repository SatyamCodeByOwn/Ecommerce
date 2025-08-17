package app.ecom.services;

import app.ecom.dto.mappers.OrderItemMapper;
import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.OrderRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public OrderItemResponseDto addItemToOrder(int userId, OrderItemRequestDto orderItemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Order order = orderRepository.findByUserAndStatus(user, Order.OrderStatus.PENDING)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(Order.OrderStatus.PENDING);
                    return orderRepository.save(newOrder);
                });

        Product product = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + orderItemRequestDto.getProductId()));

        Optional<OrderItem> existingItemOptional = orderItemRepository.findByOrderAndProduct(order, product);

        OrderItem orderItem;
        if (existingItemOptional.isPresent()) {
            orderItem = existingItemOptional.get();
            orderItem.setQuantity(orderItem.getQuantity() + orderItemRequestDto.getQuantity());
        } else {
            orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequestDto.getQuantity());
            orderItem.setPrice(product.getPrice());
        }

        OrderItem savedItem = orderItemRepository.save(orderItem);

        // Update the order's total amount after saving the item
        updateOrderTotal(order);

        return OrderItemMapper.toResponseDTO(savedItem);
    }

    public OrderItemResponseDto updateOrderItemQuantity(int itemId, int quantity) {
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + itemId));

        orderItem.setQuantity(quantity);

        OrderItem updatedItem = orderItemRepository.save(orderItem);

        // Update the order's total amount after updating the item
        updateOrderTotal(updatedItem.getOrder());

        return OrderItemMapper.toResponseDTO(updatedItem);
    }

    public void removeOrderItem(int itemId) {
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem not found with id: " + itemId));

        Order order = orderItem.getOrder();

        orderItemRepository.delete(orderItem);

        // Update the order's total amount after removing the item
        updateOrderTotal(order);
    }

    public List<OrderItemResponseDto> getItemsByOrderId(int orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return items.stream()
                .map(OrderItemMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Private helper method to calculate and update the total amount of an order.
     * @param order The order entity to update.
     */
    private void updateOrderTotal(Order order) {
        // Since the order's item list is lazy-loaded, we need to re-fetch it or make sure it's loaded.
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        double newTotalAmount = orderItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        order.setTotalAmount(newTotalAmount);
        orderRepository.save(order);
    }
}