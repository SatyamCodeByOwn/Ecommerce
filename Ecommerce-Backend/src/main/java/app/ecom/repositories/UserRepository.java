package app.ecom.repositories;

import app.ecom.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByPhoneNumber(@NotBlank(message = "Phone number is required") @Size(max = 20, message = "Phone number must not exceed 20 characters") String phoneNumber);
}
