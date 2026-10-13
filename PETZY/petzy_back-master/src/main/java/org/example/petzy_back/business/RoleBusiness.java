package org.example.petzy_back.business;

import org.example.petzy_back.dto.catalog.CatalogRequestDTO;
import org.example.petzy_back.entity.catalog.Role;
import org.example.petzy_back.repository.catalog.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleBusiness {

    @Autowired
    private RoleRepository roleRepository;

    public void validateRoleData(CatalogRequestDTO request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio.");
        }
    }

    public Role createEntityFromRequest(CatalogRequestDTO request) {
        validateRoleData(request);
        Role role = new Role();
        role.setName(request.getName());
        return role;
    }
}
