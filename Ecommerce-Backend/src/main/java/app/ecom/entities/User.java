package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(length = 30, nullable = false, unique = true)
    private String userName;

    @Column(length = 30, nullable = false, unique = true)
    private String userEmail;

    @Column(length = 10, nullable = false)
    private String userPasswordSalt;

    @Column(length = 64, nullable = false)
    private String userPasswordHash;

    @Column(length = 20, nullable = false, unique = true)
    private String userPhoneNumber;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
