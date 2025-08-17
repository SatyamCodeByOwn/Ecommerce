package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import java.io.IOException;

public class SellerMapper {

    public static Seller toEntity(SellerRequestDTO dto, User user) throws IOException {
        Seller seller = new Seller();
        seller.setUser(user);
        seller.setStoreName(dto.getStoreName());
        seller.setGstNumber(dto.getGstNumber());
        seller.setPanCard(dto.getPanCard().getBytes());
        seller.setApprovalStatus(Seller.ApprovalStatus.PENDING);
        return seller;
    }

    public static SellerResponseDTO toDTO(Seller seller) {
        return new SellerResponseDTO(
                seller.getId(),
                seller.getUser().getId(),
                seller.getStoreName(),
                seller.getGstNumber(),
                seller.getApprovalStatus()
        );
    }

    public static void updateEntity(Seller seller, SellerRequestDTO dto, User user) throws IOException {
        seller.setStoreName(dto.getStoreName());
        seller.setGstNumber(dto.getGstNumber());
        seller.setUser(user);
        seller.setPanCard(dto.getPanCard().getBytes());
    }
}