//package app.ecom.controller;
//
//import app.ecom.dto.request_dto.ShippingAddressRequestDTO;
//import app.ecom.dto.response_dto.ShippingAddressResponseDTO;
//import app.ecom.service.ShippingAddressService; // You will need to create this service
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/shipping-addresses")
//@RequiredArgsConstructor
//public class ShippingAddressController {
//
//    private final ShippingAddressService shippingAddressService; // Inject your service
//
//    /**
//     * Endpoint to add a new shipping address for a user.
//     *
//     * @param requestDTO The DTO containing the address details.
//     * @return A ResponseEntity with the created ShippingAddressResponseDTO and HTTP status 201 (Created).
//     */
//    @PostMapping
//    public ResponseEntity<ShippingAddressResponseDTO> addShippingAddress(@Valid @RequestBody ShippingAddressRequestDTO requestDTO) {
//        ShippingAddressResponseDTO createdAddress = shippingAddressService.addShippingAddress(requestDTO);
//        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
//    }
//
//    /**
//     * Endpoint to retrieve all shipping addresses for a specific user.
//     *
//     * @param userId The ID of the user.
//     * @return A ResponseEntity containing a list of ShippingAddressResponseDTOs.
//     */
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<ShippingAddressResponseDTO>> getAddressesByUserId(@PathVariable int userId) {
//        List<ShippingAddressResponseDTO> addresses = shippingAddressService.getAddressesByUserId(userId);
//        return ResponseEntity.ok(addresses);
//    }
//
//    /**
//     * Endpoint to retrieve a single shipping address by its ID.
//     *
//     * @param id The ID of the shipping address.
//     * @return A ResponseEntity containing the ShippingAddressResponseDTO.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<ShippingAddressResponseDTO> getAddressById(@PathVariable int id) {
//        ShippingAddressResponseDTO address = shippingAddressService.getAddressById(id);
//        return ResponseEntity.ok(address);
//    }
//
//    /**
//     * Endpoint to update an existing shipping address.
//     *
//     * @param id         The ID of the address to update.
//     * @param requestDTO The DTO with the updated address details.
//     * @return A ResponseEntity containing the updated ShippingAddressResponseDTO.
//     */
//    @PutMapping("/{id}")
//    public ResponseEntity<ShippingAddressResponseDTO> updateShippingAddress(@PathVariable int id, @Valid @RequestBody ShippingAddressRequestDTO requestDTO) {
//        ShippingAddressResponseDTO updatedAddress = shippingAddressService.updateShippingAddress(id, requestDTO);
//        return ResponseEntity.ok(updatedAddress);
//    }
//
//    /**
//     * Endpoint to delete a shipping address by its ID.
//     *
//     * @param id The ID of the address to delete.
//     * @return A ResponseEntity with HTTP status 204 (No Content).
//     */
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteShippingAddress(@PathVariable int id) {
//        shippingAddressService.deleteShippingAddress(id);
//        return ResponseEntity.noContent().build();
//    }
//}
