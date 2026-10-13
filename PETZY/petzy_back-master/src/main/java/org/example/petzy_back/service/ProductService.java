package org.example.petzy_back.service;


import org.example.petzy_back.dto.catalog.CatalogResponseDTO;
import org.example.petzy_back.repository.ProductCategoryRepository;
import org.springframework.transaction.annotation.Transactional;
import org.example.petzy_back.business.ProductBusiness;
import org.example.petzy_back.dto.productDTO.ProductRequestDTO;
import org.example.petzy_back.dto.productDTO.ProductResponseDTO;
import org.example.petzy_back.entity.Product;
import org.example.petzy_back.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductBusiness productBusiness;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findByStatusTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return mapToDTO(product);
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product product = productBusiness.createEntityFromRequest(requestDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Product existingProduct = findProductOrThrow(id);

        Product updatedProductData = productBusiness.updateEntityFromRequest(existingProduct, requestDTO);
        Product savedProduct = productRepository.save(updatedProductData);

        return mapToDTO(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        product.setStatus(false);
        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<CatalogResponseDTO> getAllProductCategories() {
        return productCategoryRepository.findAll().stream()
                .map(category -> {
                    CatalogResponseDTO dto = new CatalogResponseDTO();
                    dto.setId(category.getId());
                    dto.setName(category.getName());
                    dto.setDescription(category.getDescription());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndStatusTrue(categoryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar privado para reutilizar la búsqueda
    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    // Método auxiliar de mapeo (Entity a DTO)
    private ProductResponseDTO mapToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setStatus(product.getStatus());

        // Mapeamos la entidad ProductCategory al CatalogResponseDTO
        if (product.getCategory() != null) {
            CatalogResponseDTO categoryDto = new CatalogResponseDTO();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setDescription(product.getCategory().getDescription());
            dto.setCategory(categoryDto);
        }

        return dto;
    }
}