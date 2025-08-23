package app.ecom.services;

import app.ecom.dto.mappers.SellerMapper;
import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.*;
import app.ecom.repositories.OrderItemRepository;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
    }

    @Transactional
    public SellerResponseDTO createSeller(int requesterId, SellerRequestDTO dto) {
        User loggedInUser = getAuthenticatedUser();

        if (loggedInUser.getId() != requesterId || requesterId != dto.getUserId()) {
            throw new UserNotAuthorizedException("You are not authorized to create this seller profile.");
        }

        if (sellerRepository.existsByGstNumber(dto.getGstNumber())) {
            throw new ResourceAlreadyExistsException("Seller", "GST Number", dto.getGstNumber());
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        if (user.getRole().getId() != 2) {
            throw new NotASellerException("You are not a seller");
        }

        try {
            Seller seller = SellerMapper.toEntity(dto, user);
            Seller savedSeller = sellerRepository.save(seller);
            return SellerMapper.toDTO(savedSeller);
        } catch (IOException e) {
            throw new FileStorageException("Failed to read PAN card file");
        }
    }

    @Transactional
    public SellerResponseDTO getSellerById(int requesterId, int sellerId) {
        User loggedInUser = getAuthenticatedUser();

        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        if (loggedInUser.getRole().getId() != 1 && seller.getUser().getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to view this seller's data.");
        }

        return SellerMapper.toDTO(seller);
    }

    public List<SellerResponseDTO> getAllSellers() {
        return sellerRepository.findAll()
                .stream()
                .map(SellerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SellerResponseDTO updateSeller(int requesterId, int sellerId, SellerRequestDTO dto) {
        User loggedInUser = getAuthenticatedUser();

        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        if (seller.getUser().getId() != requesterId || dto.getUserId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to update this seller profile.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        if (sellerRepository.existsByGstNumberAndIdNot(dto.getGstNumber(), sellerId)) {
            throw new ResourceAlreadyExistsException("Seller", "GST Number", dto.getGstNumber());
        }

        try {
            SellerMapper.updateEntity(seller, dto, user);
            Seller updatedSeller = sellerRepository.save(seller);
            return SellerMapper.toDTO(updatedSeller);
        } catch (IOException e) {
            throw new FileStorageException("Failed to read PAN card file");
        }
    }

    @Transactional
    public void deleteSeller(int requesterId, int sellerId) {
        User loggedInUser = getAuthenticatedUser();

        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        if (loggedInUser.getRole().getId() != 1 && seller.getUser().getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to delete this seller profile.");
        }

        sellerRepository.deleteById(sellerId);
    }

    public SellerResponseDTO approveSeller(int sellerId) {
        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        seller.setApprovalStatus(Seller.ApprovalStatus.APPROVED);
        sellerRepository.save(seller);

        return SellerMapper.toDTO(seller);
    }

    public SellerResponseDTO rejectSeller(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        seller.setApprovalStatus(Seller.ApprovalStatus.REJECTED);
        sellerRepository.save(seller);

        return SellerMapper.toDTO(seller);
    }

    public double getTotalRevenue(int sellerUserId) {
        User loggedInUser = getAuthenticatedUser();

        if (loggedInUser.getId() != sellerUserId) {
            throw new UserNotAuthorizedException("You are not authorized to view this revenue data.");
        }

        Double revenue = orderItemRepository.getTotalRevenueBySeller(sellerUserId);
        return revenue != null ? revenue : 0.0;
    }
}
