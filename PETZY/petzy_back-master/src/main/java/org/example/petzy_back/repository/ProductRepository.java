package org.example.petzy_back.repository;

import org.example.petzy_back.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusTrue();
    List<Product> findByCategoryIdAndStatusTrue(Long categoryId);
}
