package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.OrderItemRequestDto;
import app.ecom.dto.response_dto.OrderItemResponseDto;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Product;

public class OrderItemMapper {

    public static OrderItem toEntity(OrderItemRequestDto dto, Product product) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(product.getPrice());
        return orderItem;
    }

    public static OrderItemResponseDto toDTO(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setId(orderItem.getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());
        dto.setOrderId(orderItem.getOrder().getId());
        return dto;
    }
}