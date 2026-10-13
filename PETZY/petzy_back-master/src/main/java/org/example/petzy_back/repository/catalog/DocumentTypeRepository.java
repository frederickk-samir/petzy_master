package org.example.petzy_back.repository.catalog;

import org.example.petzy_back.entity.catalog.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
