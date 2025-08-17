package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.Commission;
import app.ecom.entities.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class CommissionMapper {

    public CommissionResponseDTO toResponseDTO(Commission commission) {
        if (commission == null) {
            return null;
        }
        CommissionResponseDTO dto = new CommissionResponseDTO();
        dto.setCommissionId(commission.getCommissionId());
        dto.setOrderItemId(commission.getOrderItem().getId());
        dto.setPlatformFee(commission.getPlatformFee());
        dto.setCommissionPercentage(commission.getCommissionPercentage());
        dto.setCommissionAmount(commission.getCommissionAmount());
        return dto;
    }

    // The toEntity method is simplified as it will not receive commissionAmount from the DTO.
    public Commission toEntity(CommissionRequestDTO requestDTO, OrderItem orderItem) {
        if (requestDTO == null) {
            return null;
        }
        Commission commission = new Commission();
        commission.setOrderItem(orderItem);
        commission.setPlatformFee(requestDTO.getPlatformFee());
        commission.setCommissionPercentage(requestDTO.getCommissionPercentage());
        // commissionAmount will be calculated and set in the service layer
        return commission;
    }
}