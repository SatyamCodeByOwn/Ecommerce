package app.ecom.controller;

import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.SellerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// @SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<SellerResponseDTO>> createSeller(
            @Valid @ModelAttribute SellerRequestDTO sellerRequestDTO
    ) {
        SellerResponseDTO createdSeller = sellerService.createSeller(sellerRequestDTO);
        return new ResponseEntity<>(
                ApiResponse.<SellerResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Seller created successfully")
                        .data(createdSeller)
                        .build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SellerResponseDTO>> getSellerById(@PathVariable int id) {
        SellerResponseDTO seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(
                ApiResponse.<SellerResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Seller fetched successfully")
                        .data(seller)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SellerResponseDTO>>> getAllSellers() {
        List<SellerResponseDTO> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(
                ApiResponse.<List<SellerResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Sellers fetched successfully")
                        .data(sellers)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SellerResponseDTO>> updateSeller(
            @PathVariable int id,
            @Valid @ModelAttribute SellerRequestDTO sellerRequestDTO
    ) {
        SellerResponseDTO updatedSeller = sellerService.updateSeller(id, sellerRequestDTO);
        return ResponseEntity.ok(
                ApiResponse.<SellerResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Seller updated successfully")
                        .data(updatedSeller)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSeller(@PathVariable int id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Seller deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<SellerResponseDTO>> approveSeller(@PathVariable int id) {
        SellerResponseDTO approvedSeller = sellerService.approveSeller(id);
        return ResponseEntity.ok(
                ApiResponse.<SellerResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Seller approved successfully")
                        .data(approvedSeller)
                        .build()
        );
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<SellerResponseDTO>> rejectSeller(@PathVariable int id) {
        SellerResponseDTO rejectedSeller = sellerService.rejectSeller(id);
        return ResponseEntity.ok(
                ApiResponse.<SellerResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Seller rejected successfully")
                        .data(rejectedSeller)
                        .build()
        );
    }
}
