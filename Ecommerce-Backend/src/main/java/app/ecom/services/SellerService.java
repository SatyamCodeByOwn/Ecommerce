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

    @Transactional
    public SellerResponseDTO createSeller(int requesterId, SellerRequestDTO dto) {
        // Get authenticated user's email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        // Check if requesterId matches the logged-in user's ID
        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        if (requesterId != dto.getUserId()) {
            throw new UserNotAuthorizedException("You cannot create a seller profile for another user.");
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + requesterId));

        if (requester.getRole().getId() != 1 && seller.getUser().getId() != requesterId) {
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // email used as username in Basic Auth

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        // Check if requesterId matches the logged-in user's ID
        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        // Ensure the seller belongs to the requester and the DTO is also for the same user
        if (seller.getUser().getId() != requesterId || dto.getUserId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to update this seller profile.");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        if (sellerRepository.existsByGstNumberAndIdNot(dto.getGstNumber(),sellerId)) {
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
        // Get authenticated user's email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // email used as username in Basic Auth

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        // Check if requesterId matches the logged-in user's ID
        if (loggedInUser.getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to perform this operation.");
        }

        Seller seller = sellerRepository.findByUserId(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));

        // Allow deletion only if requester is admin or owns the seller profile
        if (loggedInUser.getRole().getId() != 1 && seller.getUser().getId() != requesterId) {
            throw new UserNotAuthorizedException("You are not authorized to delete this seller profile.");
        }

        sellerRepository.deleteById(sellerId);
    }


    public SellerResponseDTO approveSeller(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        seller.setApprovalStatus(Seller.ApprovalStatus.APPROVED);
        sellerRepository.save(seller);

        return SellerMapper.toDTO(seller); // mapping here
    }

    public SellerResponseDTO rejectSeller(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        seller.setApprovalStatus(Seller.ApprovalStatus.REJECTED);
        sellerRepository.save(seller);

        return SellerMapper.toDTO(seller); // mapping here
    }

    @Autowired
    private OrderItemRepository orderItemRepository;

    public double getTotalRevenue(int sellerUserId) {
        // Get authenticated user's email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // email used as username in Basic Auth

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        // Ensure the logged-in user is the same as the one in the path
        if (loggedInUser.getId() != sellerUserId) {
            throw new UserNotAuthorizedException("You are not authorized to view this revenue data.");
        }

        Double revenue = orderItemRepository.getTotalRevenueBySeller(sellerUserId);
        return revenue != null ? revenue : 0.0;
    }


}