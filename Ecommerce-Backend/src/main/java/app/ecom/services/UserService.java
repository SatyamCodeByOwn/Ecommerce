package app.ecom.services;

import app.ecom.dto.mappers.UserMapper;
import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.exceptions.custom.RoleNotAllowedException;
import app.ecom.exceptions.custom.UserNotAuthorizedException;
import app.ecom.repositories.RoleRepository;
import app.ecom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (loggedInUser.getId() != id && loggedInUser.getRole().getId() != 1) {
            throw new UserNotAuthorizedException("You are not authorized to view this user's details.");
        }


        return UserMapper.toResponseDTO(loggedInUser);
    }


    @Transactional
    public UserResponseDTO updateUser(int requesterId, int userId, UserRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (loggedInUser.getId() != requesterId || requesterId != userId) {
            throw new UserNotAuthorizedException("You are not authorized to update another user's profile.");
        }

        User user = loggedInUser;

        if (userRepository.existsByUsernameAndIdNot(dto.getUsername(), userId)) {
            throw new ResourceAlreadyExistsException("User", "username", dto.getUsername());
        }

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), userId)) {
            throw new ResourceAlreadyExistsException("User", "email", dto.getEmail());
        }

        // Prevent assigning OWNER role
        if (dto.getRoleId() == 1) {
            throw new RoleNotAllowedException("Role OWNER is not allowed.");
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        UserMapper.updateEntity(user, dto, role, hashedPassword);
        User updatedUser = userRepository.save(user);

        return UserMapper.toResponseDTO(updatedUser);
    }


    @Transactional
    public void deactivateUser(int requesterId, int deactivateUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        // Allow only self or owner to deactivate
        if (requesterId != deactivateUserId && loggedInUser.getRole().getId() != 1) {
            throw new UserNotAuthorizedException("You are not authorized to deactivate this user.");
        }

        User targetUser = userRepository.findById(deactivateUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + deactivateUserId));

        targetUser.setActive(false);
        userRepository.save(targetUser);
    }


    @Transactional
    public void activateUser(int requesterId, int targetUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        // Only owner can activate users
        if (loggedInUser.getRole().getId() != 1) {
            throw new UserNotAuthorizedException("Only the owner can activate users.");
        }

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + targetUserId));

        targetUser.setActive(true);
        userRepository.save(targetUser);
    }
}