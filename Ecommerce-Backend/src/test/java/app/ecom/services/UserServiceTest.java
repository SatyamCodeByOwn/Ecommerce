package app.ecom.services;

import app.ecom.dto.mappers.UserMapper;
import app.ecom.dto.request_dto.UserRequestDTO;
import app.ecom.dto.response_dto.UserResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.*;
import app.ecom.repositories.RoleRepository;
import app.ecom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserService userService;

    private UserRequestDTO requestDTO;
    private Role roleCustomer;
    private Role roleOwner;
    private User user;

    @BeforeEach
    void setUp() {
        roleCustomer = new Role(2, Role.RoleName.CUSTOMER);
        roleOwner = new Role(1, Role.RoleName.OWNER);

        requestDTO = new UserRequestDTO(
                "testUser",
                "test@example.com",
                "password123",
                "1234567890",
                2
        );

        user = new User(
                1,
                "testUser",
                "test@example.com",
                "hashedPass",
                "1234567890",
                roleCustomer,
                true
        );
    }

    private void mockSecurityContext(String email) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(email);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void registerUser_Success() {
        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByPhoneNumber("1234567890")).thenReturn(false);
        when(roleRepository.findById(2)).thenReturn(Optional.of(roleCustomer));
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
    void registerUser_ShouldThrow_WhenOwnerRoleIsUsed() {
        requestDTO.setRoleId(1);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(any())).thenReturn(false);
        when(roleRepository.findById(1)).thenReturn(Optional.of(roleOwner));

        assertThrows(RoleNotAllowedException.class, () -> userService.registerUser(requestDTO));
    }

    @Test
    void updateUser_Success() {
        mockSecurityContext("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByUsernameAndIdNot("testUser", 1)).thenReturn(false);
        when(userRepository.existsByEmailAndIdNot("test@example.com", 1)).thenReturn(false);
        when(roleRepository.findById(2)).thenReturn(Optional.of(roleCustomer));
        when(passwordEncoder.encode("password123")).thenReturn("hashedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);

        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.updateEntity(any(), any(), any(), any())).then(inv -> null);
            mockedMapper.when(() -> UserMapper.toResponseDTO(any())).thenReturn(new UserResponseDTO());

            UserResponseDTO result = userService.updateUser(1, 1, requestDTO);
            assertThat(result).isNotNull();
            verify(userRepository).save(any(User.class));
        }
    }

    @Test
    void updateUser_ShouldThrow_WhenUnauthorized() {
        mockSecurityContext("test@example.com");
        User otherUser = new User(2, "other", "test@example.com", "pass", "8888888888", roleCustomer, true);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(otherUser));

        assertThrows(UserNotAuthorizedException.class, () -> userService.updateUser(1, 2, requestDTO));
    }

    @Test
    void updateUser_ShouldThrow_WhenOwnerRoleIsUsed() {
        mockSecurityContext("test@example.com");
        requestDTO.setRoleId(1);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(RoleNotAllowedException.class, () -> userService.updateUser(1, 1, requestDTO));
    }

    @Test
    void deactivateUser_Success_BySelf() {
        mockSecurityContext("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deactivateUser(1, 1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deactivateUser_Success_ByOwner() {
        mockSecurityContext("owner@example.com");
        User owner = new User(2, "owner", "owner@example.com", "pass", "9999999999", roleOwner, true);
        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(owner));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deactivateUser(2, 1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deactivateUser_ShouldThrow_WhenUnauthorized() {
        mockSecurityContext("other@example.com");
        User otherUser = new User(3, "other", "other@example.com", "pass", "8888888888", roleCustomer, true);
        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));

        assertThrows(UserNotAuthorizedException.class, () -> userService.deactivateUser(3, 1));
    }

    @Test
    void activateUser_Success_ByOwner() {
        mockSecurityContext("owner@example.com");
        User owner = new User(2, "owner", "owner@example.com", "pass", "9999999999", roleOwner, true);
        user.setActive(false);

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(owner));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        userService.activateUser(2, 1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void activateUser_ShouldThrow_WhenUnauthorized() {
        mockSecurityContext("other@example.com");
        User otherUser = new User(3, "other", "other@example.com", "pass", "8888888888", roleCustomer, true);

        when(userRepository.findByEmail("other@example.com")).thenReturn(Optional.of(otherUser));

        assertThrows(UserNotAuthorizedException.class, () -> userService.activateUser(3, 1));
    }


    @Test
    void getAllUsers_ShouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO());
            List<UserResponseDTO> result = userService.getAllUsers();
            assertThat(result).hasSize(1);
        }
    }

    @Test
    void getUserById_Success() {
        mockSecurityContext("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        try (MockedStatic<UserMapper> mockedMapper = mockStatic(UserMapper.class)) {
            mockedMapper.when(() -> UserMapper.toResponseDTO(user)).thenReturn(new UserResponseDTO());
            UserResponseDTO result = userService.getUserById(1);
            assertThat(result).isNotNull();
        }
    }

    @Test
    void getUserById_ShouldThrow_WhenNotFound() {
        mockSecurityContext("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1));
    }
}
