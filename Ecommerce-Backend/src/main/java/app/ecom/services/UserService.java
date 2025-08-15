package app.ecom.services;

import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;
import app.ecom.exceptions.ResourceAlreadyExistsException;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.dto.mappers.UserMapper;
import app.ecom.repositories.RoleRepository;
import app.ecom.repositories.UserRepository;
import app.ecom.utility.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        // Check for username uniqueness
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("User","username",dto.getUsername());
        }

        // Check for email uniqueness
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email","email",dto.getEmail());
        }

        // Get role
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        // Generate salt & hash
        String salt = Utils.generateSalt();
        String newPassword = salt + dto.getPassword();
        String hash = Utils.generateHash(newPassword);

        // Convert DTO → Entity
        User user = UserMapper.toEntity(dto, role, salt, hash);

        // Save user
        User savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return UserMapper.toResponseDTO(user);
    }
}