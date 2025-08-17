package app.ecom.repositories;

import app.ecom.entities.Categories;
import app.ecom.entities.Categories.CategoryName; // Import the nested enum
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

    // Find a Category entity by its enum name
    Optional<Categories> findByName(CategoryName name);
}