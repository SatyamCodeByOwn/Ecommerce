package app.ecom.controller;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.services.CommissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    @Autowired
    private CommissionService commissionService;

    /**
     * Creates a new commission record for an order item.
     * Commissions are typically created automatically after an order item is finalized.
     *
     * @param commissionRequestDTO DTO containing the order item ID.
     * @return ResponseEntity with the created CommissionResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<CommissionResponseDTO> createCommission(@Valid @RequestBody CommissionRequestDTO commissionRequestDTO) {
        CommissionResponseDTO createdCommission = commissionService.createCommission(commissionRequestDTO);
        return new ResponseEntity<>(createdCommission, HttpStatus.CREATED);
    }

    /**
     * Retrieves a commission by its ID.
     *
     * @param id The ID of the commission to retrieve.
     * @return ResponseEntity containing the CommissionResponseDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommissionResponseDTO> getCommissionById(@PathVariable int id) {
        CommissionResponseDTO commission = commissionService.getCommissionById(id);
        return ResponseEntity.ok(commission);
    }

    /**
     * Retrieves all commissions associated with a specific order.
     *
     * @param orderId The ID of the order.
     * @return ResponseEntity containing a list of CommissionResponseDTOs.
     */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<CommissionResponseDTO>> getCommissionsByOrderId(@PathVariable int orderId) {
        List<CommissionResponseDTO> commissions = commissionService.getCommissionsByOrderId(orderId);
        return ResponseEntity.ok(commissions);
    }

    // Note: Update and Delete operations for commissions are often not exposed via API
    // as commissions are generally considered immutable financial records once created.
    // If business rules require them, you would add methods here.
}