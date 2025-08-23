package app.ecom.services;

import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.entities.Role;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.*;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock private SellerRepository sellerRepository;
    @Mock private UserRepository userRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private MultipartFile panCardFile;
    @Mock private Authentication authentication;

    @InjectMocks private SellerService sellerService;

    private User user;
    private Seller seller;
    private SellerRequestDTO sellerRequestDTO;

    private static final int SELLER_ROLE_ID = 2;
    private static final int CUSTOMER_ROLE_ID = 3;
    private static final int OWNER_ROLE_ID = 1;

    @BeforeEach
    void setUp() {
        Role sellerRole = new Role();
        sellerRole.setId(SELLER_ROLE_ID);

        user = new User();
        user.setId(1);
        user.setEmail("seller@example.com");
        user.setRole(sellerRole);

        seller = new Seller();
        seller.setId(1);
        seller.setUser(user);
        seller.setStoreName("Test Store");
        seller.setGstNumber("GST1234567890123");

        sellerRequestDTO = new SellerRequestDTO(
                user.getId(), "Test Store", "GST1234567890123", panCardFile
        );

        lenient().when(authentication.getName()).thenReturn(user.getEmail());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createSeller_success() throws Exception {
        when(panCardFile.getBytes()).thenReturn("PAN123".getBytes());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.createSeller(user.getId(), sellerRequestDTO);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void createSeller_invalidRole_throwsNotASellerException() {
        user.getRole().setId(CUSTOMER_ROLE_ID);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);

        assertThrows(NotASellerException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void createSeller_duplicateGst_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void createSeller_userNotFound_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void createSeller_panCardIOException_throwsException() throws Exception {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(panCardFile.getBytes()).thenThrow(new IOException("Error reading PAN card"));

        assertThrows(FileStorageException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void getSellerById_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.of(seller));

        SellerResponseDTO response = sellerService.getSellerById(user.getId(), seller.getId());

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void getSellerById_notFound_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.getSellerById(user.getId(), seller.getId()));
    }

    @Test
    void getAllSellers_success() {
        when(sellerRepository.findAll()).thenReturn(Collections.singletonList(seller));

        var response = sellerService.getAllSellers();

        assertEquals(1, response.size());
        assertEquals("Test Store", response.get(0).getStoreName());
    }

    @Test
    void updateSeller_success() throws Exception {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.of(seller));
        when(sellerRepository.existsByGstNumberAndIdNot(sellerRequestDTO.getGstNumber(), seller.getId())).thenReturn(false);
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);
        when(panCardFile.getBytes()).thenReturn("PAN123".getBytes());

        SellerResponseDTO response = sellerService.updateSeller(user.getId(), seller.getId(), sellerRequestDTO);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void updateSeller_notFound_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.updateSeller(user.getId(), seller.getId(), sellerRequestDTO));
    }

    @Test
    void deleteSeller_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.of(seller));

        sellerService.deleteSeller(user.getId(), seller.getId());

        verify(sellerRepository).deleteById(seller.getId());
    }

    @Test
    void deleteSeller_notFound_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.deleteSeller(user.getId(), seller.getId()));
    }

    @Test
    void approveSeller_success() {
        when(sellerRepository.findByUserId(seller.getId())).thenReturn(Optional.of(seller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.approveSeller(seller.getId());

        assertEquals(Seller.ApprovalStatus.APPROVED, response.getApprovalStatus());
    }

    @Test
    void rejectSeller_success() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.rejectSeller(seller.getId());

        assertEquals(Seller.ApprovalStatus.REJECTED, response.getApprovalStatus());
    }

    @Test
    void getTotalRevenue_success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(orderItemRepository.getTotalRevenueBySeller(user.getId())).thenReturn(200.0);

        double revenue = sellerService.getTotalRevenue(user.getId());

        assertEquals(200.0, revenue);
    }
}
