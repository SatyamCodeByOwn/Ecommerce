package app.ecom.services;

import app.ecom.dto.mappers.SellerMapper;
import app.ecom.dto.request_dto.SellerRequestDTO;
import app.ecom.dto.response_dto.SellerResponseDTO;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import app.ecom.exceptions.ResourceAlreadyExistsException;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SellerResponseDTO createSeller(SellerRequestDTO dto) throws IOException {
        if (sellerRepository.existsByGstNumber(dto.getGstNumber())) {
            throw new ResourceAlreadyExistsException("Seller", "GST Number", dto.getGstNumber());
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        Seller seller = SellerMapper.toEntity(dto, user);
        Seller savedSeller = sellerRepository.save(seller);

        return SellerMapper.toDTO(savedSeller);
    }

    public SellerResponseDTO getSellerById(int id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));
        return SellerMapper.toDTO(seller);
    }

    public List<SellerResponseDTO> getAllSellers() {
        return sellerRepository.findAll()
                .stream()
                .map(SellerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SellerResponseDTO updateSeller(int id, SellerRequestDTO dto) throws IOException {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        SellerMapper.updateEntity(seller, dto, user);
        Seller updatedSeller = sellerRepository.save(seller);

        return SellerMapper.toDTO(updatedSeller);
    }

    @Transactional
    public void deleteSeller(int id) {
        if (!sellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seller not found with id: " + id);
        }
        sellerRepository.deleteById(id);
    }
}
