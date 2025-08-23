package app.ecom.services;

import app.ecom.dto.mappers.OrderMapper;
import app.ecom.dto.request_dto.OrderRequestDTO;
import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderResponseDTO;
import app.ecom.entities.*;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.exceptions.custom.SellerNotAuthorizedException;
import app.ecom.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final OrderItemService orderItemService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SellerRepository sellerRepository;

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    private boolean isOwnerOrSelf(User loggedInUser, int targetUserId) {
        return loggedInUser.getId() == targetUserId || loggedInUser.getRole().getId() == 1;
    }

    private boolean isOwnerOrSelf(User loggedInUser, User resourceOwner) {
        return loggedInUser.getId() == resourceOwner.getId() || loggedInUser.getRole().getId() == 1;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        User loggedInUser = getLoggedInUser();
        if (!isOwnerOrSelf(loggedInUser, orderRequestDTO.getUserId())) {
            throw new ResourceNotFoundException("Access denied: Cannot create order for another user");
        }

        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + orderRequestDTO.getUserId()));

        ShippingAddress shippingAddress = null;
        if (orderRequestDTO.getShippingAddressId() != null) {
            shippingAddress = shippingAddressRepository.findById(orderRequestDTO.getShippingAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Shipping address not found with ID: " + orderRequestDTO.getShippingAddressId()));
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(0.0);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDto itemDto : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + itemDto.getProductId()));
            if (product.getStock() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);
            totalAmount += product.getPrice() * itemDto.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for user id: " + user.getId()));

        cartItemRepository.deleteAllByCartId(cart.getId());

        return OrderMapper.toDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        User loggedInUser = getLoggedInUser();
        if (!isOwnerOrSelf(loggedInUser, order.getUser())) {
            throw new ResourceNotFoundException("Access denied: Cannot view another user's order");
        }

        return OrderMapper.toDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByUserId(int userId) {
        User loggedInUser = getLoggedInUser();
        if (!isOwnerOrSelf(loggedInUser, userId)) {
            throw new ResourceNotFoundException("Access denied: Cannot view another user's orders");
        }

        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders == null || orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user with ID: " + userId);
        }

        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(int sellerId, int id, String status) {
        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller with ID " + sellerId + " does not exist."));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        boolean isSellerOfThisOrder = order.getOrderItems().stream()
                .anyMatch(orderItem -> orderItem.getProduct().getSeller().getId() == sellerId);

        if (!isSellerOfThisOrder) {
            throw new SellerNotAuthorizedException("Seller with ID " + sellerId + " is not authorized to update this order.");
        }

        Order.OrderStatus currentStatus = order.getStatus();
        Order.OrderStatus newStatus;

        try {
            newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }

        switch (currentStatus) {
            case PENDING:
                if (newStatus != Order.OrderStatus.PROCESSING && newStatus != Order.OrderStatus.CANCELLED) {
                    throw new IllegalStateException("A PENDING order can only be moved to PROCESSING or CANCELLED status.");
                }
                break;
            case PROCESSING:
                if (newStatus != Order.OrderStatus.SHIPPED && newStatus != Order.OrderStatus.CANCELLED) {
                    throw new IllegalStateException("A PROCESSING order can only be moved to SHIPPED or CANCELLED status.");
                }
                break;
            case SHIPPED:
                if (newStatus != Order.OrderStatus.DELIVERED) {
                    throw new IllegalStateException("A SHIPPED order can only be moved to DELIVERED status.");
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new IllegalStateException("A " + currentStatus + " order status cannot be changed.");
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toDTO(updatedOrder);
    }

    @Transactional
    public void deleteOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        User loggedInUser = getLoggedInUser();
        if (!isOwnerOrSelf(loggedInUser, order.getUser())) {
            throw new ResourceNotFoundException("Access denied: Cannot delete another user's order");
        }

        orderRepository.delete(order);
    }
}
