package app.ecom.services;

import app.ecom.dto.mappers.OrderMapper;
import app.ecom.dto.request_dto.OrderRequestDTO;
import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderResponseDTO;
import app.ecom.entities.*;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.SellerNotAuthorizedException;
import app.ecom.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final OrderItemService orderItemService;
    private final CartRepository cartRepository; // <-- YEH ADD KAREIN
    private final CartItemRepository cartItemRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
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

        // 2. Uss cart ke saare items delete karein (METHOD CALL UPDATE KIYA GAYA)
        cartItemRepository.deleteAllByCartId(cart.getId());


        return OrderMapper.toDTO(savedOrder);
    }

    public OrderResponseDTO getOrderById(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
        return OrderMapper.toDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(int sellerId, int id, String status) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller with ID " + sellerId + " does not exist."));

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));

        // Step 2: Check karein ki kya order ka koi bhi product is seller ka hai
        // Yeh product table ke seller_id column se compare karega
        boolean isSellerOfThisOrder = order.getOrderItems().stream()
                .anyMatch(orderItem -> orderItem.getProduct().getSeller().getId() == sellerId);



        if (!isSellerOfThisOrder) {
            // Agar seller maalik nahi hai, to exception throw karein
            throw new SellerNotAuthorizedException("Seller with ID " + sellerId + " is not authorized to update this order.");
        }

        // Step 3: Agar seller authorized hai, to order ka status update karein
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(newStatus);
            Order updatedOrder = orderRepository.save(order);
            return OrderMapper.toDTO(updatedOrder);
        } catch (IllegalArgumentException e) {
            // Agar status invalid hai, to exception throw karein
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    @Transactional
    public void cancelOrder(int id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
        orderRepository.delete(order);
    }
}