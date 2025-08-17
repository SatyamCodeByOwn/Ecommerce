package app.ecom.controller;

import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.services.SellerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<SellerResponseDTO> createSeller(
            @Valid @ModelAttribute SellerRequestDTO sellerRequestDTO
    ) {
        SellerResponseDTO createdSeller = sellerService.createSeller(sellerRequestDTO);
        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> getSellerById(@PathVariable int id) {
        SellerResponseDTO seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @GetMapping
    public ResponseEntity<List<SellerResponseDTO>> getAllSellers() {
        List<SellerResponseDTO> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SellerResponseDTO> updateSeller(
            @PathVariable int id,
            @Valid @ModelAttribute SellerRequestDTO sellerRequestDTO
    ) {
        SellerResponseDTO updatedSeller = sellerService.updateSeller(id, sellerRequestDTO);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable int id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
}