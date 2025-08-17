package app.ecom.services;

import app.ecom.dto.mappers.WishlistMapper;
import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.entities.Wishlist;
import app.ecom.entities.WishlistItem;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import app.ecom.repositories.WishlistItemRepository;
import app.ecom.repositories.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private WishlistItemRepository wishlistItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    @Transactional
    public WishlistResponseDTO getOrCreateWishlist(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Optional<Wishlist> wishlistOptional = wishlistRepository.findByUserId(userId);

        Wishlist wishlist = wishlistOptional.orElseGet(() -> {
            Wishlist newWishlist = new Wishlist();
            newWishlist.setUser(user);
            return wishlistRepository.save(newWishlist);
        });

        return WishlistMapper.toResponseDTO(wishlist);
    }

    @Transactional
    public WishlistResponseDTO addProductToWishlist(int userId, int productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Optional<WishlistItem> itemOptional = wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId);

        if (itemOptional.isEmpty()) {
            WishlistItem newWishlistItem = new WishlistItem();
            newWishlistItem.setWishlist(wishlist);
            newWishlistItem.setProduct(product);
            wishlistItemRepository.save(newWishlistItem);

            wishlist = wishlistRepository.findById(wishlist.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found after saving new item."));
        }

        return WishlistMapper.toResponseDTO(wishlist);
    }

    @Transactional
    public WishlistResponseDTO removeProductFromWishlist(int userId, int productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user with id: " + userId));

        WishlistItem wishlistItem = wishlistItemRepository.findByWishlistIdAndProductId(wishlist.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in the wishlist."));

        wishlistItemRepository.delete(wishlistItem);

        return WishlistMapper.toResponseDTO(wishlist);
    }
}