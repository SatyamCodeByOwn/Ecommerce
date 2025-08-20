package app.ecom.services;

import app.ecom.dto.mappers.UserMapper;
import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.repositories.RoleRepository;
import app.ecom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRequestDTO requestDTO;
    private Role role;
    private User user;

    @BeforeEach
    void setUp() {
        requestDTO = new UserRequestDTO(
                "testUser",
                "test@example.com",
                "password123",
                "1234567890",
                1
        );

        role = new Role(1, Role.RoleName.CUSTOMER);

        user = new User(
                1,
                "testUser",
                "test@example.com",
                "hashedPass",
                "1234567890",
                role
        );
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.toEntity(any(), any(), any())).thenReturn(user);
            mockedMapper.when(() -> UserMapper.toResponseDTO(any())).thenReturn(new UserResponseDTO());

            UserResponseDTO result = userService.registerUser(requestDTO);

            assertThat(result).isNotNull();
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void registerUser_ShouldThrow_WhenUsernameExists() {
        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.registerUser(requestDTO));
    }

    @Test
    void registerUser_ShouldThrow_WhenEmailExists() {
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> userService.registerUser(requestDTO));
    }

    @Test
    void registerUser_ShouldThrow_WhenRoleNotFound() {
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.registerUser(requestDTO));
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO());

            List<UserResponseDTO> result = userService.getAllUsers();

            assertThat(result).hasSize(1);
        }
    }

    @Test
    void getUserById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO());

            UserResponseDTO result = userService.getUserById(1);

            assertThat(result).isNotNull();
        }
    }

    @Test
    void getUserById_ShouldThrow_WhenNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.updateEntity(any(), any(), any(), any())).thenAnswer(inv -> null);
            mockedMapper.when(() -> UserMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO());

            UserResponseDTO result = userService.updateUser(1, requestDTO);

            assertThat(result).isNotNull();
            verify(userRepository).save(any(User.class));
        }
    }


    @Test
    void updateUser_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1, requestDTO));
    }

    @Test
    void updateUser_ShouldThrow_WhenRoleNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(1, requestDTO));
    }

    @Test
    void deleteUser_Success() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_ShouldThrow_WhenNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(1));
    }
}
