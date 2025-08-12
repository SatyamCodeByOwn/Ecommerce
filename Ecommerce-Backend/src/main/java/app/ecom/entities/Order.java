package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_table") // 'order' is a reserved keyword in SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private double totalAmount = 0.00;

    @Column(length = 50, nullable = false)
    private String orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderDate;
}
