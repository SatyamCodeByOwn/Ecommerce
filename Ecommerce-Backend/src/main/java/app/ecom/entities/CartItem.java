package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"}) // ✅ ensures one product per cart
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"cart", "product"}) // ✅ avoids infinite recursion
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // ✅ safer equality
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // ✅ equality based only on ID
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity = 1;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAdded;

    // ✅ set timestamp automatically before persisting
    @PrePersist
    protected void onCreate() {
        this.dateAdded = LocalDateTime.now();
    }
}
