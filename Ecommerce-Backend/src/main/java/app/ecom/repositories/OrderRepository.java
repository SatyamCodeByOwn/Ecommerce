package app.ecom.repositories;

import app.ecom.entities.Order;
import app.ecom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByUserAndStatus(User user, Order.OrderStatus status);

    List<Order> findByUserId(int userId);
}