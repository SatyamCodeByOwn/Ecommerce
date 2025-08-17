package app.ecom.services;

import app.ecom.dto.mappers.CartItemMapper;
import app.ecom.dto.mappers.CartMapper;
import app.ecom.dto.request_dto.CartItemRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.entities.Cart;
import app.ecom.entities.CartItem;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.repositories.CartItemRepository;
import app.ecom.repositories.CartRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartResponseDTO getOrCreateCart(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        if (existingCart.isPresent()) {
            return CartMapper.toDTO(existingCart.get());
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            Cart savedCart = cartRepository.save(newCart);
            return CartMapper.toDTO(savedCart);
        }
    }

    @Transactional
    public CartResponseDTO addProductToCart(int userId, CartItemRequestDTO dto) {
        // Corrected Logic: First, get or create the cart to ensure it exists.
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + dto.getProductId()));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(dto.getQuantity());
            cartItemRepository.save(newItem);
        }

        // Return the updated cart
        return CartMapper.toDTO(cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found after update")));
    }

    @Transactional
    public CartResponseDTO updateCartItemQuantity(int userId, int productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user ID: " + userId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for product ID: " + productId));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        return CartMapper.toDTO(cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found after update")));
    }

    @Transactional
    public CartResponseDTO removeProductFromCart(int userId, int productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user ID: " + userId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for product ID: " + productId));

        cartItemRepository.delete(cartItem);

        return CartMapper.toDTO(cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found after removal")));
    }

    @Transactional
    public CartResponseDTO clearCart(int userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user ID: " + userId));

        cart.getCartItems().clear();
        cartRepository.save(cart);

        return CartMapper.toDTO(cart);
    }
}