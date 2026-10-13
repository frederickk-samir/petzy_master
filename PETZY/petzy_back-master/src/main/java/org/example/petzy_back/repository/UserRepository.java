package org.example.petzy_back.repository;

import org.example.petzy_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByStatusTrue();

    Optional<User> findByEmail(String email);

    // Buscar por número de documento exacto
    Optional<User> findByDocumentNumber(String documentNumber);

    // Buscar usuarios activos cuyo nombre contenga el texto ingresado (búsqueda parcial)
    List<User> findByNameContainingIgnoreCaseAndStatusTrue(String name);
}