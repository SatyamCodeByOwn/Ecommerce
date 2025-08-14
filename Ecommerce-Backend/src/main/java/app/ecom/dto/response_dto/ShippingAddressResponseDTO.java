package app.ecom.dto.response_dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressResponseDTO {
    private int id;
    private int userId;
    private String fullName;
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
}
