package app.ecom.services;

import app.ecom.dto.mappers.OrderItemMapper;
import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.OrderRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderItemResponseDto addItemToOrder(int userId, OrderItemRequestDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Order order = orderRepository.findByUserIdAndStatus(userId, Order.OrderStatus.PENDING)
                .orElseGet(() -> {
                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(Order.OrderStatus.PENDING);
                    newOrder.setTotalAmount(0.0);
                    return orderRepository.save(newOrder);
                });

        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + itemDto.getProductId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemDto.getQuantity());
        orderItem.setPrice(product.getPrice());

        OrderItem savedItem = orderItemRepository.save(orderItem);

        order.setTotalAmount(order.getTotalAmount() + (orderItem.getQuantity() * orderItem.getPrice()));
        orderRepository.save(order);

        return OrderItemMapper.toDTO(savedItem);
    }

    public List<OrderItemResponseDto> getItemsByOrderId(int orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return items.stream()
                .map(OrderItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderItemResponseDto updateOrderItemQuantity(int itemId, int quantity) {
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with ID: " + itemId));
        orderItem.setQuantity(quantity);

        Order order = orderItem.getOrder();
        double oldTotal = order.getTotalAmount();
        double oldItemTotal = orderItem.getPrice() * (orderItem.getQuantity() - quantity); // Calculate the old item total amount
        order.setTotalAmount(oldTotal - oldItemTotal + (orderItem.getPrice() * quantity)); // Update the total amount
        orderRepository.save(order);

        OrderItem updatedItem = orderItemRepository.save(orderItem);
        return OrderItemMapper.toDTO(updatedItem);
    }

    @Transactional
    public void removeOrderItem(int itemId) {
        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with ID: " + itemId));

        Order order = orderItem.getOrder();
        order.setTotalAmount(order.getTotalAmount() - (orderItem.getPrice() * orderItem.getQuantity()));
        order.getOrderItems().remove(orderItem);

        orderRepository.save(order);
    }
}