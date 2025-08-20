package app.ecom.services;



import app.ecom.dto.request_dto.CartRequestDTO;
import app.ecom.dto.response_dto.CartResponseDTO;
import app.ecom.entities.Cart;
import app.ecom.entities.CartItem;
import app.ecom.entities.Product;
import app.ecom.entities.User;

import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.dto.mappers.CartMapper;

import app.ecom.repositories.CartItemRepository;
import app.ecom.repositories.CartRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get or create cart for a user
    public CartResponseDTO getOrCreateCart(int userId) {

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return CartMapper.toResponseDTO(cart);
    }

    // ✅ Add product to cart
    public CartResponseDTO addProductToCart(int userId, CartRequestDTO dto) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for userId: " + userId));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + dto.getProductId()));


        // check if item already exists in cart
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(cartItem.getQuantity() + dto.getQuantity());
            cartItemRepository.save(cartItem);
        } else {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(dto.getQuantity());
            cart.getCartItems().add(cartItem);
        }

        cartRepository.save(cart);
        return CartMapper.toResponseDTO(cart);
    }

    // ✅ Update product quantity
    public CartResponseDTO updateCartItemQuantity(int userId, int productId, int quantity) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for userId: " + userId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with id: " + productId));

        if (quantity <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
        }

        cartRepository.save(cart);
        return CartMapper.toResponseDTO(cart);
    }

    // ✅ Remove product from cart
    public CartResponseDTO removeProductFromCart(int userId, int productId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for userId: " + userId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart with id: " + productId));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);


        cartRepository.save(cart);
        return CartMapper.toResponseDTO(cart);
    }

    // ✅ Clear cart
    public CartResponseDTO clearCart(int userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for userId: " + userId));

        cart.getCartItems().clear();
        cartItemRepository.deleteAllByCartId(cart.getId());

        cartRepository.save(cart);
        return CartMapper.toResponseDTO(cart);


    }
}
