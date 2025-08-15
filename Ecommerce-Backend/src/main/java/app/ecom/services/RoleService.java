package app.ecom.services;

import app.ecom.dto.mappers.RoleMapper;
import app.ecom.dto.request_dto.RoleRequestDto;
import app.ecom.dto.response_dto.RoleResponseDto;
import app.ecom.entities.Role;
import app.ecom.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Creates a new role.
     *
     * @param roleRequestDto The DTO containing the role name.
     * @return The DTO of the newly created role.
     */
    public RoleResponseDto createRole(RoleRequestDto roleRequestDto) {
        Role role = RoleMapper.toEntity(roleRequestDto);
        Role savedRole = roleRepository.save(role);
        return RoleMapper.toResponseDTO(savedRole);
    }

    /**
     * Retrieves a role by its ID.
     *
     * @param id The ID of the role.
     * @return The DTO of the found role.
     */
    public RoleResponseDto getRoleById(int id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return RoleMapper.toResponseDTO(role);
    }

    /**
     * Retrieves all roles.
     *
     * @return A list of all role DTOs.
     */
    public List<RoleResponseDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(RoleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
