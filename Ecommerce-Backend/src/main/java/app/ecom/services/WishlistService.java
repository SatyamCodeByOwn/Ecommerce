package app.ecom.services;

import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.entities.Wishlist;
import app.ecom.entities.WishlistItem;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.dto.mappers.WishlistMapper;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import app.ecom.repositories.WishlistItemRepository;
import app.ecom.repositories.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                           WishlistItemRepository wishlistItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    // ✅ Get or create wishlist
    public WishlistResponseDTO getOrCreateWishlist(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(user);
                    return wishlistRepository.save(newWishlist);
                });

        return WishlistMapper.toResponseDTO(wishlist);
    }

    // ✅ Add product to wishlist
    public WishlistResponseDTO addProductToWishlist(int userId, int productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for userId: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Check if the product already exists in wishlist
        boolean exists = wishlist.getWishlistItems().stream()
                .anyMatch(item -> item.getProduct().getId() == productId);

        if (!exists) {
            WishlistItem item = new WishlistItem();
            wishlist.addWishlistItem(item); // helper sets both sides
            item.setProduct(product);
        }

        wishlistRepository.save(wishlist);
        return WishlistMapper.toResponseDTO(wishlist);
    }

    // ✅ Remove product from wishlist
    public WishlistResponseDTO removeProductFromWishlist(int userId, int productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for userId: " + userId));

        WishlistItem itemToRemove = wishlist.getWishlistItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in wishlist with id: " + productId));

        wishlist.removeWishlistItem(itemToRemove);
        wishlistItemRepository.delete(itemToRemove);

        wishlistRepository.save(wishlist);
        return WishlistMapper.toResponseDTO(wishlist);
    }

    // ✅ Clear wishlist
    public WishlistResponseDTO clearWishlist(int userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for userId: " + userId));

        wishlist.clearWishlistItems();
        wishlistItemRepository.deleteAllByWishlistId(wishlist.getId());

        wishlistRepository.save(wishlist);
        return WishlistMapper.toResponseDTO(wishlist);
    }
}
