package app.ecom.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private CategoryName name;

    // The enum is kept within the entity class
    public enum CategoryName {
        ELECTRONICS,
        FASHION,
        HOME_APPLIANCES,
        BOOKS,
        TOYS,
        SPORTS,
        BEAUTY
    }
}