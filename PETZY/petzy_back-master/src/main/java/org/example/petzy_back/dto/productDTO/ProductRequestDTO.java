package org.example.petzy_back.dto.productDTO;

import lombok.Data;

@Data
public class ProductRequestDTO {

    private String name;
    private Double price;
    private Integer stock;
    private Long categoryId;

}
