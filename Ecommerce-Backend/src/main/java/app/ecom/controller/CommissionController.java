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


    @PostMapping
    public ResponseEntity<CommissionResponseDTO> createCommission(@Valid @RequestBody CommissionRequestDTO commissionRequestDTO) {
        CommissionResponseDTO createdCommission = commissionService.createCommission(commissionRequestDTO);
        return new ResponseEntity<>(createdCommission, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CommissionResponseDTO> getCommissionById(@PathVariable int id) {
        CommissionResponseDTO commission = commissionService.getCommissionById(id);
        return ResponseEntity.ok(commission);
    }


    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<CommissionResponseDTO>> getCommissionsByOrderId(@PathVariable int orderId) {
        List<CommissionResponseDTO> commissions = commissionService.getCommissionsByOrderId(orderId);
        return ResponseEntity.ok(commissions);
    }


}