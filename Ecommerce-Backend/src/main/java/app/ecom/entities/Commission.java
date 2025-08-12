package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "commissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commissionId;

    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    @Column(nullable = false)
    private double platformFee;

    @Column(nullable = false)
    private double commissionPercentage;

    @Column(nullable = false)
    private double commissionAmount;
}
