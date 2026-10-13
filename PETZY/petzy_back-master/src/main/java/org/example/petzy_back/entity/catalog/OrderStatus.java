package org.example.petzy_back.entity.catalog;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_statuses")
@Data
public class OrderStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
