package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {

    private int id;
    private int userId;
    private List<Integer> orderItemIds;
    private String status;
    private double totalAmount;
    private LocalDateTime orderDate;
    private Integer shippingAddressId;
}