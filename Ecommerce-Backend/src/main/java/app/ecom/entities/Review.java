package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) // Reviews are made by a User (customer)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY) // A review is for a specific Product
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private double rating; // E.g., 1.0 to 5.0

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDate submissionDate;

    // The seller is associated with the product, so no direct 'seller' field is needed here.
    // You can get the seller via review.getProduct().getSeller()

    @PrePersist
    protected void onCreate() {
        this.submissionDate = LocalDate.now(); // Automatically sets the submission date on creation
    }
}