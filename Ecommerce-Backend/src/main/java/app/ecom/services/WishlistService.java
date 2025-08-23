package app.ecom.services;

import app.ecom.dto.mappers.WishlistMapper;
import app.ecom.dto.response_dto.WishlistResponseDTO;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.entities.Wishlist;
import app.ecom.entities.WishlistItem;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.exceptions.custom.RoleNotAllowedException;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import app.ecom.repositories.WishlistItemRepository;
import app.ecom.repositories.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private User getAuthenticatedCustomer(int userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User loggedInUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));

        if (loggedInUser.getId() != userId) {
            throw new RoleNotAllowedException("You are not authorized to access another user's wishlist.");
        }

        if (loggedInUser.getRole().getId() != 3) {
            throw new RoleNotAllowedException("Only customers can access or modify wishlists.");
        }

        return loggedInUser;
    }

    public WishlistResponseDTO getOrCreateWishlist(int userId) {
        User customer = getAuthenticatedCustomer(userId);

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUser(customer);
                    return wishlistRepository.save(newWishlist);
                });

        return WishlistMapper.toResponseDTO(wishlist);
    }

    public WishlistResponseDTO addProductToWishlist(int userId, int productId) {
        getAuthenticatedCustomer(userId);

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for userId: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        boolean exists = wishlist.getWishlistItems().stream()
                .anyMatch(item -> item.getProduct().getId() == productId);

        if (!exists) {
            WishlistItem item = new WishlistItem();
            wishlist.addWishlistItem(item);
            item.setProduct(product);
        }

        wishlistRepository.save(wishlist);
        return WishlistMapper.toResponseDTO(wishlist);
    }

    public WishlistResponseDTO removeProductFromWishlist(int userId, int productId) {
        getAuthenticatedCustomer(userId);

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

    public WishlistResponseDTO clearWishlist(int userId) {
        getAuthenticatedCustomer(userId);

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for userId: " + userId));

        wishlist.clearWishlistItems();
        wishlistItemRepository.deleteAllByWishlistId(wishlist.getId());

        wishlistRepository.save(wishlist);
        return WishlistMapper.toResponseDTO(wishlist);
    }
}
