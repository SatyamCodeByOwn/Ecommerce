package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wishlist_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "product_id"}) // ✅ ensures one product per wishlist
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"wishlist", "product"}) // ✅ avoids infinite recursion
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ✅ safer equality
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ equality based only on ID
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wishlist_id", nullable = false)
    private Wishlist wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDateTime dateAdded;

    // ✅ set timestamp automatically before persisting
    @PrePersist
    protected void onCreate() {
        this.dateAdded = LocalDateTime.now();
    }
}
