//package app.ecom.controller;
//
//import app.ecom.dto.request_dto.CommissionRequestDTO;
//import app.ecom.dto.response_dto.CommissionResponseDTO;
//import app.ecom.service.CommissionService; // You will need to create this service
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/commissions")
//@RequiredArgsConstructor
//public class CommissionController {
//
//    private final CommissionService commissionService; // Inject your commission service
//
//    /**
//     * Endpoint to create a new commission record for an order item.
//     *
//     * @param commissionRequestDTO The DTO containing the commission details.
//     * @return A ResponseEntity with the created CommissionResponseDTO and HTTP status 201 (Created).
//     */
//    @PostMapping
//    public ResponseEntity<CommissionResponseDTO> createCommission(@Valid @RequestBody CommissionRequestDTO commissionRequestDTO) {
//        CommissionResponseDTO createdCommission = commissionService.createCommission(commissionRequestDTO);
//        return new ResponseEntity<>(createdCommission, HttpStatus.CREATED);
//    }
//
//    /**
//     * Endpoint to retrieve a commission by its unique ID.
//     *
//     * @param id The ID of the commission to retrieve.
//     * @return A ResponseEntity containing the CommissionResponseDTO.
//     */
//    @GetMapping("/{id}")
//    public ResponseEntity<CommissionResponseDTO> getCommissionById(@PathVariable int id) {
//        CommissionResponseDTO commission = commissionService.getCommissionById(id);
//        return ResponseEntity.ok(commission);
//    }
//
//    /**
//     * Endpoint to retrieve the commission for a specific order item.
//     *
//     * @param orderItemId The ID of the order item.
//     * @return A ResponseEntity containing the CommissionResponseDTO.
//     */
//    @GetMapping("/order-item/{orderItemId}")
//    public ResponseEntity<CommissionResponseDTO> getCommissionByOrderItemId(@PathVariable int orderItemId) {
//        CommissionResponseDTO commission = commissionService.getCommissionByOrderItemId(orderItemId);
//        return ResponseEntity.ok(commission);
//    }
//}
