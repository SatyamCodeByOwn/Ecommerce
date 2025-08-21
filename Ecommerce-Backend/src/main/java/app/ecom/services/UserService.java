package app.ecom.services;

import app.ecom.dto.mappers.UserMapper;
import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.exceptions.custom.RoleNotAllowedException;
import app.ecom.repositories.RoleRepository;
import app.ecom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO dto) {

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", dto.getUsername());
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", dto.getEmail());
        }

        if (userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
            throw new ResourceAlreadyExistsException("User", "phone number", dto.getPhoneNumber());
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        if (dto.getRoleId()==1) {
            throw new RoleNotAllowedException("Role OWNER is not allowed for registration");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = UserMapper.toEntity(dto, role, hashedPassword);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return UserMapper.toResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(int id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check for duplicate username/email only if they have changed
        if (!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new ResourceAlreadyExistsException("User", "username", dto.getUsername());
        }
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("User", "email", dto.getEmail());
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        UserMapper.updateEntity(user, dto, role, hashedPassword);
        User updatedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}