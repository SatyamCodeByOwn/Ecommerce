package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.ShippingAddressRequestDTO;
import app.ecom.dto.response_dto.ShippingAddressResponseDTO;
import app.ecom.entities.ShippingAddress;
import app.ecom.entities.User;

public class ShippingAddressMapper {

    public static ShippingAddress toEntity(ShippingAddressRequestDTO dto, User user) {
        ShippingAddress address = new ShippingAddress();
        address.setUser(user);
        address.setFullName(dto.getFullName());
        address.setAddressLine(dto.getAddressLine());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setPhoneNumber(dto.getPhoneNumber());
        return address;
    }

    public static ShippingAddressResponseDTO toDTO(ShippingAddress address) {
        return new ShippingAddressResponseDTO(
                address.getId(),
                address.getUser().getId(),
                address.getFullName(),
                address.getAddressLine(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.getPhoneNumber()
        );
    }

    public static void updateEntity(ShippingAddress address, ShippingAddressRequestDTO dto, User user) {
        address.setUser(user);
        address.setFullName(dto.getFullName());
        address.setAddressLine(dto.getAddressLine());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setPhoneNumber(dto.getPhoneNumber());
    }
}