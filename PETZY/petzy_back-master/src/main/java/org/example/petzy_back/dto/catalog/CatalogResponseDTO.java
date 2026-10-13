package org.example.petzy_back.dto.catalog;

import lombok.Data;

@Data
public class CatalogResponseDTO {
    private Long id;
    private String name;
    private String description;
}
