package org.example.petzy_back.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.petzy_back.entity.catalog.ProductCategory;

@Entity
@Table(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    private Boolean status = true;

    // Relación Many-to-One con la categoría normalizada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory category;
}