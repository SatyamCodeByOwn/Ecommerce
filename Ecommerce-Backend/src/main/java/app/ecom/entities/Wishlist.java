package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wishlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<WishlistItem> wishlistItems = new HashSet<>();

    // ✅ Helper methods for bidirectional sync
    public void addWishlistItem(WishlistItem item) {
        wishlistItems.add(item);
        item.setWishlist(this);
    }

    public void removeWishlistItem(WishlistItem item) {
        wishlistItems.remove(item);
        item.setWishlist(null);
    }

    public void clearWishlistItems() {
        for (WishlistItem item : new HashSet<>(wishlistItems)) {
            removeWishlistItem(item);
        }
    }
}
