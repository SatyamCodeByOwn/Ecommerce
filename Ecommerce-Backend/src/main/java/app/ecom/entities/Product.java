package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @Column(length = 50, nullable = false)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String productDescription;

    @Column(nullable = false)
    private double productPrice = 0.00;

    @Column(nullable = false)
    private int productStock = 0;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @Column(length = 255)
    private String productImage;
}
