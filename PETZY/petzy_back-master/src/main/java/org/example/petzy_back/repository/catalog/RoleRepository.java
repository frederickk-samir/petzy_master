package org.example.petzy_back.repository.catalog;

import org.example.petzy_back.entity.catalog.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}