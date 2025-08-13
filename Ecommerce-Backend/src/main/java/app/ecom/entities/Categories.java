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
    private int categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private CategoryName categoryName;

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
