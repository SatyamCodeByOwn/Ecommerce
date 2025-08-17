package app.ecom.repositories;

import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // This method finds an OrderItem by its associated Order and Product
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);

    List<OrderItem> findByOrderId(int orderId);
}