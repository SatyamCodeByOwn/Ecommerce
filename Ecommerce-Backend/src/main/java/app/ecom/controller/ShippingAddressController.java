package app.ecom.controller;

import app.ecom.dto.request_dto.ShippingAddressRequestDTO;
import app.ecom.dto.response_dto.ShippingAddressResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.ShippingAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/shipping-addresses")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    // CREATE a new shipping address
    @PostMapping
    public ResponseEntity<ApiResponse<ShippingAddressResponseDTO>> createShippingAddress(
            @Valid @RequestBody ShippingAddressRequestDTO dto
    ) {
        ShippingAddressResponseDTO newAddress = shippingAddressService.createShippingAddress(dto);
        return new ResponseEntity<>(
                ApiResponse.<ShippingAddressResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Shipping address created successfully")
                        .data(newAddress)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ all shipping addresses for a specific user
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<ShippingAddressResponseDTO>>> getAddressesByUserId(@PathVariable int userId) {
        List<ShippingAddressResponseDTO> addresses = shippingAddressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(
                ApiResponse.<List<ShippingAddressResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Shipping addresses fetched successfully")
                        .data(addresses)
                        .build()
        );
    }

    // READ a single shipping address by ID (optional)
    // Uncomment if you want single fetch
//    @GetMapping("/{id}")
//    public ResponseEntity<ApiResponse<ShippingAddressResponseDTO>> getShippingAddressById(@PathVariable int id) {
//        ShippingAddressResponseDTO address = shippingAddressService.getShippingAddressById(id);
//        return ResponseEntity.ok(
//                ApiResponse.<ShippingAddressResponseDTO>builder()
//                        .status(HttpStatus.OK.value())
//                        .message("Shipping address fetched successfully")
//                        .data(address)
//                        .build()
//        );
//    }

    // UPDATE an existing shipping address
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingAddressResponseDTO>> updateShippingAddress(
            @PathVariable int id,
            @Valid @RequestBody ShippingAddressRequestDTO dto
    ) {
        ShippingAddressResponseDTO updatedAddress = shippingAddressService.updateShippingAddress(id, dto);
        return ResponseEntity.ok(
                ApiResponse.<ShippingAddressResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Shipping address updated successfully")
                        .data(updatedAddress)
                        .build()
        );
    }

    // DELETE a shipping address by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShippingAddress(@PathVariable int id) {
        shippingAddressService.deleteShippingAddress(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Shipping address deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
