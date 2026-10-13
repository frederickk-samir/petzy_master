package org.example.petzy_back.service;

import jakarta.transaction.Transactional;
import org.example.petzy_back.business.CatalogBusiness;
import org.example.petzy_back.dto.catalog.CatalogRequestDTO;
import org.example.petzy_back.dto.catalog.CatalogResponseDTO;
import org.example.petzy_back.entity.catalog.ProductCategory;
import org.example.petzy_back.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private CatalogBusiness catalogBusiness;

    @Transactional
    public CatalogResponseDTO createProductCategory(CatalogRequestDTO requestDTO) {

        ProductCategory category = catalogBusiness.createCategoryEntity(requestDTO);

        ProductCategory savedCategory = productCategoryRepository.save(category);

        CatalogResponseDTO responseDTO = new CatalogResponseDTO();
        responseDTO.setId(savedCategory.getId());
        responseDTO.setName(savedCategory.getName());
        responseDTO.setDescription(savedCategory.getDescription());

        return responseDTO;
    }
}