package org.example.petzy_back.controller;

import org.example.petzy_back.dto.catalog.CatalogRequestDTO;
import org.example.petzy_back.dto.catalog.CatalogResponseDTO;
import org.example.petzy_back.service.CatalogService;
import org.example.petzy_back.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogs")
@CrossOrigin(origins = "*")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private RoleService roleService;

    @PostMapping("/product-categories")
    public ResponseEntity<CatalogResponseDTO> createProductCategory(@RequestBody CatalogRequestDTO requestDTO) {
        CatalogResponseDTO created = catalogService.createProductCategory(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<CatalogResponseDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @PostMapping("/roles")
    public ResponseEntity<CatalogResponseDTO> createRole(@RequestBody CatalogRequestDTO requestDTO) {
        CatalogResponseDTO created = roleService.createRole(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}