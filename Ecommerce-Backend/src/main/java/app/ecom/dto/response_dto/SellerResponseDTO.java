package app.ecom.dto.response_dto;

import app.ecom.entities.Seller.ApprovalStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponseDTO {
    private int id;
    private int userId;
    private String storeName;
    private String gstNumber;
    private ApprovalStatus approvalStatus;
}
