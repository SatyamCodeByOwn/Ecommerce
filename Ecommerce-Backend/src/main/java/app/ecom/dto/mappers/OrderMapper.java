package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.OrderRequestDTO;
import app.ecom.dto.response_dto.OrderResponseDTO;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.ShippingAddress;
import app.ecom.entities.User;

import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity(OrderRequestDTO dto, User user,
                                 java.util.List<OrderItem> orderItems,
                                 ShippingAddress shippingAddress) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setStatus(Order.OrderStatus.valueOf(dto.getStatus().toUpperCase()));
        order.setTotalAmount(dto.getTotalAmount());
        order.setOrderDate(dto.getOrderDate());
        order.setShippingAddress(shippingAddress);
        return order;
    }

    public static OrderResponseDTO toDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setOrderItemIds(order.getOrderItems()
                .stream()
                .map(OrderItem::getId)
                .collect(Collectors.toList()));
        dto.setStatus(order.getStatus().name());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderDate(order.getOrderDate());
        dto.setShippingAddressId(order.getShippingAddress() != null
                ? order.getShippingAddress().getId()
                : null);
        return dto;
    }
}
