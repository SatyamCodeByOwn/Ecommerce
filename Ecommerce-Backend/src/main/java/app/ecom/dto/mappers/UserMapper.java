package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto, Role role, String passwordHash) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordHash);
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(role);
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRoleName(user.getRole().getName().name());
        return dto;
    }

    public static void updateEntity(User user, UserRequestDTO dto, Role role, String passwordHash) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordHash);
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(role);
    }
}