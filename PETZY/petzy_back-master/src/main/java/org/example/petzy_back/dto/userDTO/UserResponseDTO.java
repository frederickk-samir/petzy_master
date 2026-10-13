package org.example.petzy_back.dto.userDTO;

import lombok.Data;
import org.example.petzy_back.dto.catalog.CatalogResponseDTO;
import org.example.petzy_back.entity.catalog.DocumentType;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Boolean status;
    private CatalogResponseDTO documentType;
    private String documentNumber;
    private String address;
    private CatalogResponseDTO role;

}
