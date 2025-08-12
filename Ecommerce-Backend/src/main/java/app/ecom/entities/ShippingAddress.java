package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipping_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shippingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 100, nullable = false)
    private String fullName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String addressLine;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 50, nullable = false)
    private String state;

    @Column(length = 20, nullable = false)
    private String postalCode;

    @Column(length = 50, nullable = false)
    private String country;

    @Column(length = 20, nullable = false)
    private String phoneNumber;
}
