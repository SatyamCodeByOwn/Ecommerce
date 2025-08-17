package app.ecom.services;

import app.ecom.dto.mappers.CartMapper;
import app.ecom.dto.request_dto.CartItemRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.entities.Cart;
import app.ecom.entities.CartItem;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.CartItemRepository;
import app.ecom.repositories.CartRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    // Get a user's cart, or create a new empty one if it doesn't exist
    @Transactional
    public CartResponseDTO getOrCreateCart(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return CartMapper.toResponseDTO(cart);
    }

    // Add a product to the cart or update its quantity if already present
    @Transactional
    public CartResponseDTO addProductToCart(int userId, CartItemRequestDTO dto) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            // Update quantity for existing item
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
        } else {
            // Create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(dto.getQuantity());
        }
        cartItemRepository.save(cartItem);

        // Re-fetch the cart to ensure the DTO is built with the updated items
        cart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found after item update."));

        return CartMapper.toResponseDTO(cart);
    }

    // Update the quantity of an item already in the cart
    @Transactional
    public CartResponseDTO updateCartItemQuantity(int userId, int productId, int newQuantity) {
        if (newQuantity <= 0) {
            return removeProductFromCart(userId, productId); // Remove if quantity is 0 or less
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in the cart."));

        cartItem.setQuantity(newQuantity);
        cartItemRepository.save(cartItem);

        // Re-fetch the cart to ensure the DTO is built with the updated items
        cart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found after item quantity update."));

        return CartMapper.toResponseDTO(cart);
    }

    // Remove a product from the cart
    @Transactional
    public CartResponseDTO removeProductFromCart(int userId, int productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found in the cart."));

        cartItemRepository.delete(cartItem);

        // Re-fetch the cart to ensure the DTO is built with the updated items
        cart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found after item removal."));

        return CartMapper.toResponseDTO(cart);
    }

    // Clear all items from a user's cart
    @Transactional
    public CartResponseDTO clearCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + userId));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear(); // Clear the set in memory

        // No need to save cart explicitly, as orphanRemoval=true handles deletions
        // Re-fetch to confirm and return
        cart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found after clearing."));

        return CartMapper.toResponseDTO(cart);
    }
}