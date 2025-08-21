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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private MultipartFile panCardFile;

    @InjectMocks
    private SellerService sellerService;

    private User user;
    private Seller seller;
    private SellerRequestDTO sellerRequestDTO;

    @BeforeEach
    void setUp() {
        Role sellerRole = new Role();
        sellerRole.setId(2); // Seller role

        user = new User();
        user.setId(1);
        user.setRole(sellerRole);

        seller = new Seller();
        seller.setId(1);
        seller.setUser(user);
        seller.setStoreName("Test Store");
        seller.setGstNumber("GST1234567890123");

        sellerRequestDTO = new SellerRequestDTO(
                user.getId(), "Test Store", "GST1234567890123", panCardFile
        );
    }

    @Test
    void createSeller_success() throws Exception {
        when(panCardFile.getBytes()).thenReturn("PAN123".getBytes());
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.createSeller(user.getId(), sellerRequestDTO);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void createSeller_invalidRole_throwsNotASellerException() {
        Role customerRole = new Role();
        customerRole.setId(3); // Not a seller

        user.setRole(customerRole);

        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(NotASellerException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void createSeller_duplicateGst_throwsException() {
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void createSeller_userNotFound_throwsException() {
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void createSeller_panCardIOException_throwsException() throws Exception {
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(panCardFile.getBytes()).thenThrow(new IOException("Error reading PAN card"));

        assertThrows(FileStorageException.class,
                () -> sellerService.createSeller(user.getId(), sellerRequestDTO));
    }

    @Test
    void getSellerById_success() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        SellerResponseDTO response = sellerService.getSellerById(user.getId(), seller.getId());

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void getSellerById_notFound() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.empty());

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
        when(panCardFile.getBytes()).thenReturn("PAN123".getBytes());
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.existsByGstNumberAndIdNot(sellerRequestDTO.getGstNumber(), seller.getId())).thenReturn(false);
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.updateSeller(user.getId(), seller.getId(), sellerRequestDTO);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

//    @Test
//    void updateSeller_invalidRole_throwsNotASellerException() {
//        Role customerRole = new Role();
//        customerRole.setId(3); // Not a seller
//
//        user.setRole(customerRole);
//
//        // Ensure seller is properly initialized
//        seller = new Seller();
//        seller.setId(1);
//        seller.setUser(user);
//        seller.setStoreName("Test Store");
//        seller.setGstNumber("GST1234567890123");
//
//        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
//        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
//        when(sellerRepository.existsByGstNumberAndIdNot(sellerRequestDTO.getGstNumber(), seller.getId())).thenReturn(false);
//
//        assertThrows(NotASellerException.class,
//                () -> sellerService.updateSeller(user.getId(), seller.getId(), sellerRequestDTO));
//    }


    @Test
    void updateSeller_notFound_throwsException() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.updateSeller(user.getId(), seller.getId(), sellerRequestDTO));
    }

    @Test
    void deleteSeller_success() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        sellerService.deleteSeller(user.getId(), seller.getId());

        verify(sellerRepository).deleteById(seller.getId());
    }

    @Test
    void deleteSeller_invalidRole_throwsNotASellerException() {
        Role customerRole = new Role();
        customerRole.setId(3); // Not a seller

        user.setRole(customerRole);

        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(NotASellerException.class,
                () -> sellerService.deleteSeller(user.getId(), seller.getId()));
    }

    @Test
    void deleteSeller_notFound_throwsException() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.deleteSeller(user.getId(), seller.getId()));
    }

    @Test
    void approveSeller_success() {
        when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
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
        when(orderItemRepository.getTotalRevenueBySeller(user.getId())).thenReturn(200.0);

        double revenue = sellerService.getTotalRevenue(user.getId());

        assertEquals(200.0, revenue);
    }

    @Test
    void getTotalRevenue_null_returnsZero() {
        when(orderItemRepository.getTotalRevenueBySeller(user.getId())).thenReturn(null);

        double revenue = sellerService.getTotalRevenue(user.getId());

        assertEquals(0.0, revenue);
    }
}