package app.ecom.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Size(max = 30)
    @Column(name = "username", length = 30, nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Size(max = 30)
    @Column(name = "email", length = 30, nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 64)
    @Column(name = "password_hash", length = 64, nullable = false)
    @ToString.Exclude
    private String passwordHash;

    @NotBlank
    @Size(max = 20)
    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // ✅ Needed for Spring Security
    public String getPassword() {
        return this.passwordHash;
    }
}