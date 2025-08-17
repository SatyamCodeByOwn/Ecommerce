package app.ecom.repositories;

import app.ecom.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByUserId(Integer userId);

    Optional<Order> findByUserIdAndStatus(Integer userId, Order.OrderStatus status);
}