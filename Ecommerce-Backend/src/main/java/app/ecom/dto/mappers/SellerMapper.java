package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.entities.Seller;
import app.ecom.entities.User;

public class SellerMapper {

    // Convert SellerRequestDTO → Seller Entity
//    public static Seller toEntity(SellerRequestDTO dto, User user) {
//        Seller seller = new Seller();
//        seller.setUser(user);
//        seller.setStoreName(dto.getStoreName());
//        seller.setGstNumber(dto.getGstNumber());
//        seller.setPanCard(dto.getPanCard());
//        seller.setApprovalStatus(Seller.ApprovalStatus.PENDING); // default
//        return seller;
//    }

    // Convert Seller Entity → SellerResponseDTO
    public static SellerResponseDTO toDTO(Seller seller) {
        return new SellerResponseDTO(
                seller.getId(),
                seller.getUser().getId(),
                seller.getStoreName(),
                seller.getGstNumber(),
                seller.getApprovalStatus()
        );
    }
}
