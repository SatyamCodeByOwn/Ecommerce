package app.ecom.controller;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.CommissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    @Autowired
    private CommissionService commissionService;

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<CommissionResponseDTO>> createCommission(
            @Valid @RequestBody CommissionRequestDTO commissionRequestDTO) {
        CommissionResponseDTO createdCommission = commissionService.createCommission(commissionRequestDTO);
        return new ResponseEntity<>(
                ApiResponse.<CommissionResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Commission created successfully")
                        .data(createdCommission)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ (Single)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionResponseDTO>> getCommissionById(@PathVariable int id) {
        CommissionResponseDTO commission = commissionService.getCommissionById(id);
        return ResponseEntity.ok(
                ApiResponse.<CommissionResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Commission fetched successfully")
                        .data(commission)
                        .build()
        );
    }

    // READ (By Order ID)
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<List<CommissionResponseDTO>>> getCommissionsByOrderId(@PathVariable int orderId) {
        List<CommissionResponseDTO> commissions = commissionService.getCommissionsByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponse.<List<CommissionResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Commissions for order fetched successfully")
                        .data(commissions)
                        .build()
        );
    }

    // READ (Total Revenue for Owner)
    @GetMapping("/owner/revenue")
    public ResponseEntity<ApiResponse<BigDecimal>> getOwnerRevenue() {
        BigDecimal totalRevenue = commissionService.getOwnerRevenue();
        return ResponseEntity.ok(
                ApiResponse.<BigDecimal>builder()
                        .status(HttpStatus.OK.value())
                        .message("Total revenue fetched successfully")
                        .data(totalRevenue)
                        .build()
        );
    }

}
