package org.example.petzy_back.dto.productDTO;

import lombok.Data;
import org.example.petzy_back.dto.catalog.CatalogResponseDTO;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private Double price;
    private Integer stock;
    private Boolean status;
    private CatalogResponseDTO category;

}
