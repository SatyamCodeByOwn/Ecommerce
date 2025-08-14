package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;

public class UserMapper {

    // Convert UserRequestDTO → User (Entity)
    public static User toEntity(UserRequestDTO dto, Role role) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordSalt(dto.getPasswordSalt());
        user.setPasswordHash(dto.getPasswordHash());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(role);
        return user;
    }

    // Convert User → UserResponseDTO
    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoleName(user.getRole().getName().name());
        return dto;
    }
}
