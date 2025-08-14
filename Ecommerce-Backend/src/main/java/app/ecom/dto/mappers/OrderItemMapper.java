package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Product;

public class OrderItemMapper {


    public static OrderItem toEntity(OrderItemRequestDto dto, Order order, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(product.getPrice()); // Set price from product at time of purchase
        return orderItem;
    }


    public static OrderItemResponseDto toResponseDTO(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setTotalPrice(orderItem.getPrice() * orderItem.getQuantity());
        return dto;
    }
}
