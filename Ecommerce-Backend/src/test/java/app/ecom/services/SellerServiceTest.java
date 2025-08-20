package app.ecom.services;

import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MultipartFile panCardFile;

    @InjectMocks
    private SellerService sellerService;

    @Mock
    private OrderItemRepository orderItemRepository;



    private User user;
    private Seller seller;
    private SellerRequestDTO sellerRequestDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);

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
        when(panCardFile.getBytes()).thenReturn("PAN123".getBytes()); // ✅ stub only here
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.createSeller(sellerRequestDTO);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
        verify(sellerRepository).save(any(Seller.class));
    }

    @Test
    void createSeller_duplicateGst_throwsException() {
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> sellerService.createSeller(sellerRequestDTO));
    }

    @Test
    void createSeller_userNotFound_throwsException() {
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.createSeller(sellerRequestDTO));
    }

    @Test
    void createSeller_panCardIOException_throwsException() throws Exception {
        when(sellerRepository.existsByGstNumber(sellerRequestDTO.getGstNumber())).thenReturn(false);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(panCardFile.getBytes()).thenThrow(new IOException("Error reading PAN card"));

        assertThrows(RuntimeException.class,
                () -> sellerService.createSeller(sellerRequestDTO));
    }

    @Test
    void getSellerById_success() {
        when(sellerRepository.findById(1)).thenReturn(Optional.of(seller));

        SellerResponseDTO response = sellerService.getSellerById(1);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void getSellerById_notFound() {
        when(sellerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.getSellerById(1));
    }

    @Test
    void getAllSellers_success() {
        when(sellerRepository.findAll()).thenReturn(Collections.singletonList(seller));

        var response = sellerService.getAllSellers();

        assertEquals(1, response.size());
    }

    @Test
    void updateSeller_success() throws Exception {
        when(panCardFile.getBytes()).thenReturn("PAN123".getBytes()); // ✅ stub only here
        when(sellerRepository.findById(1)).thenReturn(Optional.of(seller));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.updateSeller(1, sellerRequestDTO);

        assertNotNull(response);
        assertEquals("Test Store", response.getStoreName());
    }

    @Test
    void updateSeller_notFound() {
        when(sellerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> sellerService.updateSeller(1, sellerRequestDTO));
    }

    @Test
    void deleteSeller_success() {
        when(sellerRepository.existsById(1)).thenReturn(true);

        doNothing().when(sellerRepository).deleteById(1);

        sellerService.deleteSeller(1);

        verify(sellerRepository, times(1)).deleteById(1);
    }


    @Test
    void deleteSeller_notFound() {
        when(sellerRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> sellerService.deleteSeller(1));
    }


    @Test
    void approveSeller_success() {
        when(sellerRepository.findById(1)).thenReturn(Optional.of(seller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.approveSeller(1);

        assertEquals(Seller.ApprovalStatus.APPROVED, response.getApprovalStatus());
    }

    @Test
    void rejectSeller_success() {
        when(sellerRepository.findById(1)).thenReturn(Optional.of(seller));
        when(sellerRepository.save(any(Seller.class))).thenReturn(seller);

        SellerResponseDTO response = sellerService.rejectSeller(1);

        assertEquals(Seller.ApprovalStatus.REJECTED, response.getApprovalStatus());
    }

    @Test
    void getTotalRevenue_success() {
        when(orderItemRepository.getTotalRevenueBySeller(user.getId()))
                .thenReturn(200.0);

        double revenue = sellerService.getTotalRevenue(user.getId());

        assertEquals(200.0, revenue);
    }

}
