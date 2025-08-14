package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.RoleRequestDto;
import app.ecom.dto.response_dto.RoleResponseDto;
import app.ecom.entities.Role;

public class RoleMapper {

    public static Role toEntity(RoleRequestDto dto) {
        Role role = new Role();
        role.setName(Role.RoleName.valueOf(dto.getName().toUpperCase()));
        return role;
    }


    public static RoleResponseDto toResponseDTO(Role role) {
        RoleResponseDto dto = new RoleResponseDto();
        dto.setId(role.getId());
        dto.setName(role.getName().name());
        return dto;
    }
}
