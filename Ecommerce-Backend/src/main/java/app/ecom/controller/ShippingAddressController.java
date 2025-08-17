package app.ecom.controller;

import app.ecom.dto.request_dto.ShippingAddressRequestDTO;
import app.ecom.dto.response_dto.ShippingAddressResponseDTO;
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
    // POST /api/shipping-addresses
    @PostMapping
    public ResponseEntity<ShippingAddressResponseDTO> createShippingAddress(@Valid @RequestBody ShippingAddressRequestDTO dto) {
        ShippingAddressResponseDTO newAddress = shippingAddressService.createShippingAddress(dto);
        return new ResponseEntity<>(newAddress, HttpStatus.CREATED);
    }

    // READ all shipping addresses for a specific user
    // GET /api/shipping-addresses/users/{userId}
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ShippingAddressResponseDTO>> getAddressesByUserId(@PathVariable int userId) {
        List<ShippingAddressResponseDTO> addresses = shippingAddressService.getAddressesByUserId(userId);
        return ResponseEntity.ok(addresses);
    }

    // READ a single shipping address by ID
    // GET /api/shipping-addresses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ShippingAddressResponseDTO> getShippingAddressById(@PathVariable int id) {
        ShippingAddressResponseDTO address = shippingAddressService.getShippingAddressById(id);
        return ResponseEntity.ok(address);
    }

    // UPDATE an existing shipping address
    // PUT /api/shipping-addresses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddressResponseDTO> updateShippingAddress(@PathVariable int id, @Valid @RequestBody ShippingAddressRequestDTO dto) {
        ShippingAddressResponseDTO updatedAddress = shippingAddressService.updateShippingAddress(id, dto);
        return ResponseEntity.ok(updatedAddress);
    }

    // DELETE a shipping address by ID
    // DELETE /api/shipping-addresses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShippingAddress(@PathVariable int id) {
        shippingAddressService.deleteShippingAddress(id);
        return ResponseEntity.noContent().build();
    }
}