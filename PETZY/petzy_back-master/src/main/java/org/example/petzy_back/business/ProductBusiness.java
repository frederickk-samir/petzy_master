package org.example.petzy_back.business;

import org.example.petzy_back.dto.productDTO.ProductRequestDTO;
import org.example.petzy_back.entity.Product;
import org.example.petzy_back.entity.catalog.ProductCategory;
import org.example.petzy_back.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductBusiness {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    // Regla de negocio: Validaciones de datos de entrada
    public void validateProductData(ProductRequestDTO request) {
        if (request.getPrice() == null || request.getPrice() < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo ni nulo.");
        }
        if (request.getStock() == null || request.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo ni nulo.");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
    }

    // Lógica para construir una entidad nueva a partir del DTO
    public Product createEntityFromRequest(ProductRequestDTO request) {
        ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoryId()));

        validateProductData(request);
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(category);
        product.setPrice(Double.valueOf(request.getPrice()));
        product.setStock(Integer.valueOf(request.getStock()));
        product.setStatus(true);
        return product;
    }

    // Lógica para actualizar una entidad existente con los datos del DTO
    public Product updateEntityFromRequest(Product existingProduct, ProductRequestDTO request) {
        ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoryId()));

        validateProductData(request);
        existingProduct.setName(request.getName());
        existingProduct.setCategory(category);
        existingProduct.setPrice(Double.valueOf(request.getPrice()));
        existingProduct.setStock(Integer.valueOf(request.getStock()));
        return existingProduct;
    }
}