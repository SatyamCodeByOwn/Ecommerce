//package app.ecom.controller;
//
//import app.ecom.dto.request_dto.SellerRequestDTO;
//import app.ecom.dto.response_dto.SellerResponseDTO;
//import app.ecom.service.SellerService; // You will need to create this service
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/sellers")
//@RequiredArgsConstructor
//public class SellerController {
//
//    private final SellerService sellerService; // Inject your seller service
//
//    /**
//     * Endpoint for a user to register as a seller.
//     *
//     * @param sellerRequestDTO The DTO containing seller registration details.
//     * @return A ResponseEntity with the created SellerResponseDTO and HTTP status 201 (Created).
//     */
//    @PostMapping("/register")
//    public ResponseEntity<SellerResponseDTO> registerSeller(@Valid @RequestBody SellerRequestDTO sellerRequestDTO) {
//        SellerResponseDTO createdSeller = sellerService.registerSeller(sellerRequestDTO);
//        return new ResponseEntity<>(createdSeller, HttpStatus.CREATED);
//    }
//
//    /**
//     * Endpoint to retrieve a seller by their ID.
//     *
//     * @param id The ID of the seller to retrieve.
//     * @return A ResponseEntity containing the SellerResponseDTO.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<SellerResponseDTO> getSellerById(@PathVariable int id) {
//        SellerResponseDTO seller = sellerService.getSellerById(id);
//        return ResponseEntity.ok(seller);
//    }
//
//    /**
//     * Endpoint to retrieve all sellers.
//     *
//     * @return A ResponseEntity containing a list of all SellerResponseDTOs.
//     */
//    @GetMapping
//    public ResponseEntity<List<SellerResponseDTO>> getAllSellers() {
//        List<SellerResponseDTO> sellers = sellerService.getAllSellers();
//        return ResponseEntity.ok(sellers);
//    }
//
//    /**
//     * Endpoint for an admin/owner to approve a seller's registration.
//     *
//     * @param id The ID of the seller to approve.
//     * @return A ResponseEntity containing the updated SellerResponseDTO.
//     */
//    @PutMapping("/{id}/approve")
//    public ResponseEntity<SellerResponseDTO> approveSeller(@PathVariable int id) {
//        SellerResponseDTO approvedSeller = sellerService.approveSeller(id);
//        return ResponseEntity.ok(approvedSeller);
//    }
//
//    /**
//     * Endpoint for an admin/owner to reject a seller's registration.
//     *
//     * @param id The ID of the seller to reject.
//     * @return A ResponseEntity containing the updated SellerResponseDTO.
//     */
//    @PutMapping("/{id}/reject")
//    public ResponseEntity<SellerResponseDTO> rejectSeller(@PathVariable int id) {
//        SellerResponseDTO rejectedSeller = sellerService.rejectSeller(id);
//        return ResponseEntity.ok(rejectedSeller);
//    }
//
//    /**
//     * Endpoint to retrieve all sellers with a PENDING approval status.
//     *
//     * @return A ResponseEntity containing a list of pending SellerResponseDTOs.
//     */
//    @GetMapping("/pending")
//    public ResponseEntity<List<SellerResponseDTO>> getPendingSellers() {
//        List<SellerResponseDTO> pendingSellers = sellerService.getPendingSellers();
//        return ResponseEntity.ok(pendingSellers);
//    }
//}
