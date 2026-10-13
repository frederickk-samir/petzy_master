package org.example.petzy_back.business;

import org.example.petzy_back.dto.userDTO.UserRequestDTO;
import org.example.petzy_back.entity.User;
import org.example.petzy_back.entity.catalog.DocumentType;
import org.example.petzy_back.entity.catalog.Role;
import org.example.petzy_back.repository.catalog.DocumentTypeRepository;
import org.example.petzy_back.repository.UserRepository;
import org.example.petzy_back.repository.catalog.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserBusiness {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private UserRepository userRepository;

    public void validateUserData(UserRequestDTO request, Long idToExclude) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario es obligatorio.");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        if (request.getDocumentNumber() == null || request.getDocumentNumber().isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio.");
        }
        if (request.getDocumentTypeId() == null) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio.");
        }
        if (request.getRoleId() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio.");
        }

        // Validar unicidad del email
        userRepository.findByEmail(request.getEmail()).ifPresent(existingUser -> {
            if (idToExclude == null || !existingUser.getId().equals(idToExclude)) {
                throw new IllegalArgumentException("El correo electrónico ya está en uso.");
            }
        });

        // Validar unicidad del número de documento
        userRepository.findByDocumentNumber(request.getDocumentNumber()).ifPresent(existingUser -> {
            if (idToExclude == null || !existingUser.getId().equals(idToExclude)) {
                throw new IllegalArgumentException("El número de documento ya está registrado.");
            }
        });
    }

    public User createEntityFromRequest(UserRequestDTO request) {
        validateUserData(request, null);

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.getRoleId()));

        DocumentType documentType = documentTypeRepository.findById(request.getDocumentTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado con ID: " + request.getDocumentTypeId()));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhone(request.getPhone());
        user.setDocumentType(documentType);
        user.setDocumentNumber(request.getDocumentNumber());
        user.setStatus(true);
        user.setRole(role);
        return user;
    }

    public User updateEntityFromRequest(User existingUser, UserRequestDTO request) {
        validateUserData(request, existingUser.getId());

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + request.getRoleId()));

        DocumentType documentType = documentTypeRepository.findById(request.getDocumentTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado con ID: " + request.getDocumentTypeId()));

        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());
        existingUser.setPhone(request.getPhone());
        existingUser.setDocumentType(documentType);
        existingUser.setDocumentNumber(request.getDocumentNumber());
        existingUser.setRole(role);
        return existingUser;
    }
}
