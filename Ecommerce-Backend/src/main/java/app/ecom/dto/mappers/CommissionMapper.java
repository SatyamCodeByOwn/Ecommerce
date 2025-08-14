package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CommissionRequestDTO;
import app.ecom.dto.response_dto.CommissionResponseDTO;
import app.ecom.entities.Commission;
import app.ecom.entities.OrderItem;

public class CommissionMapper {

    // Convert RequestDTO → Entity
    public static Commission toEntity(CommissionRequestDTO dto, OrderItem orderItem) {
        Commission commission = new Commission();
        commission.setOrderItem(orderItem);
        commission.setPlatformFee(dto.getPlatformFee());
        commission.setCommissionPercentage(dto.getCommissionPercentage());
        commission.setCommissionAmount(dto.getCommissionAmount());
        return commission;
    }

    // Convert Entity → ResponseDTO
    public static CommissionResponseDTO toResponseDTO(Commission commission) {
        CommissionResponseDTO dto = new CommissionResponseDTO();
        dto.setCommissionId(commission.getCommissionId());
        dto.setOrderItemId(commission.getOrderItem().getId());
        dto.setPlatformFee(commission.getPlatformFee());
        dto.setCommissionPercentage(commission.getCommissionPercentage());
        dto.setCommissionAmount(commission.getCommissionAmount());
        return dto;
    }
}
