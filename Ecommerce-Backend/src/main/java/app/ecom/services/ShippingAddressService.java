package app.ecom.services;

import app.ecom.dto.mappers.ShippingAddressMapper;
import app.ecom.dto.request_dto.ShippingAddressRequestDTO;
import app.ecom.dto.response_dto.ShippingAddressResponseDTO;
import app.ecom.entities.ShippingAddress;
import app.ecom.entities.User;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.ShippingAddressRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingAddressService {

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ShippingAddressResponseDTO createShippingAddress(ShippingAddressRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        ShippingAddress address = ShippingAddressMapper.toEntity(dto, user);
        ShippingAddress savedAddress = shippingAddressRepository.save(address);

        return ShippingAddressMapper.toDTO(savedAddress);
    }

    public List<ShippingAddressResponseDTO> getAddressesByUserId(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return shippingAddressRepository.findByUser(user)
                .stream()
                .map(ShippingAddressMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ShippingAddressResponseDTO getShippingAddressById(int id) {
        ShippingAddress address = shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address not found with id: " + id));
        return ShippingAddressMapper.toDTO(address);
    }

    @Transactional
    public ShippingAddressResponseDTO updateShippingAddress(int id, ShippingAddressRequestDTO dto) {
        ShippingAddress address = shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address not found with id: " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dto.getUserId()));

        ShippingAddressMapper.updateEntity(address, dto, user);
        ShippingAddress updatedAddress = shippingAddressRepository.save(address);

        return ShippingAddressMapper.toDTO(updatedAddress);
    }

    @Transactional
    public void deleteShippingAddress(int id) {
        if (!shippingAddressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Shipping address not found with id: " + id);
        }
        shippingAddressRepository.deleteById(id);
    }
}