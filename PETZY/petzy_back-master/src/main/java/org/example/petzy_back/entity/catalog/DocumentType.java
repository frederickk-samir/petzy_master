package org.example.petzy_back.entity.catalog;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "document_types")
@Data
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 10)
    private String code;
}