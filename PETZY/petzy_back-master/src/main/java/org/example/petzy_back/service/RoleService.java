package org.example.petzy_back.service;

import org.example.petzy_back.business.RoleBusiness;
import org.example.petzy_back.dto.catalog.CatalogRequestDTO;
import org.example.petzy_back.dto.catalog.CatalogResponseDTO;
import org.example.petzy_back.entity.catalog.Role;
import org.springframework.transaction.annotation.Transactional;
import org.example.petzy_back.repository.catalog.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleBusiness roleBusiness;

    @Transactional(readOnly = true)
    public List<CatalogResponseDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> {
                    CatalogResponseDTO dto = new CatalogResponseDTO();
                    dto.setId(role.getId());
                    dto.setName(role.getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CatalogResponseDTO createRole(CatalogRequestDTO requestDTO) {
        Role role = roleBusiness.createEntityFromRequest(requestDTO);
        Role savedRole = roleRepository.save(role);

        CatalogResponseDTO dto = new CatalogResponseDTO();
        dto.setId(savedRole.getId());
        dto.setName(savedRole.getName());
        return dto;
    }
}
