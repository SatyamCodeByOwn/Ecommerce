package app.ecom.services;

import app.ecom.dto.mappers.OrderMapper;
import app.ecom.dto.request_dto.OrderRequestDTO;
import app.ecom.dto.response_dto.OrderResponseDTO;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.ShippingAddress;
import app.ecom.entities.User;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.OrderRepository;
import app.ecom.repositories.ShippingAddressRepository;
import app.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  OrderItemRepository orderItemRepository;
    @Autowired
    private  ShippingAddressRepository shippingAddressRepository;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + orderRequestDTO.getUserId()));

        List<OrderItem> orderItems = orderItemRepository.findAllById(orderRequestDTO.getOrderItemIds());
        if (orderItems.size() != orderRequestDTO.getOrderItemIds().size()) {
            throw new RuntimeException("One or more order items not found.");
        }

        ShippingAddress shippingAddress = null;
        if (orderRequestDTO.getShippingAddressId() != null) {
            shippingAddress = shippingAddressRepository.findById(orderRequestDTO.getShippingAddressId())
                    .orElseThrow(() -> new RuntimeException("Shipping Address not found with id: " + orderRequestDTO.getShippingAddressId()));
        }

        Order order = OrderMapper.toEntity(orderRequestDTO, user, orderItems, shippingAddress);
        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderMapper.toDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByUserId(int userId) {
        // You will need to add this method to your OrderRepository
        List<Order> orders = orderRepository.findByUser_Id(userId);
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO updateOrderStatus(int id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        Order updatedOrder = orderRepository.save(order);

        return OrderMapper.toDTO(updatedOrder);
    }

    public void cancelOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        orderRepository.delete(order);
    }
}
