package org.example.petzy_back.business;

import org.example.petzy_back.dto.catalog.CatalogRequestDTO;
import org.example.petzy_back.entity.catalog.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class CatalogBusiness {

   // Regla de negocio: Validar datos del catálogo
    public void validateCatalogData(CatalogRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del catálogo es obligatorio.");
        }
    }

    // Construir la entidad ProductCategory a partir del request
    public ProductCategory createCategoryEntity(CatalogRequestDTO request) {
        validateCatalogData(request);
        ProductCategory category = new ProductCategory();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return category;
    }
}
