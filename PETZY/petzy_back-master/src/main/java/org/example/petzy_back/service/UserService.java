package org.example.petzy_back.service;

import org.example.petzy_back.dto.userDTO.UserRequestDTO;
import org.springframework.transaction.annotation.Transactional;
import org.example.petzy_back.business.UserBusiness;
import org.example.petzy_back.dto.catalog.CatalogResponseDTO;
import org.example.petzy_back.dto.userDTO.UserResponseDTO;
import org.example.petzy_back.entity.User;
import org.example.petzy_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBusiness userBusiness;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByStatusTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = findUserOrThrow(id);
        return mapToDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCaseAndStatusTrue(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUsersByDocument(String documentNumber) {
        User user = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con documento: " + documentNumber));
        return mapToDTO(user);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        User user = userBusiness.createEntityFromRequest(requestDTO);
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {
        User existingUser = findUserOrThrow(id);
        User updatedUserData = userBusiness.updateEntityFromRequest(existingUser, requestDTO);
        User savedUser = userRepository.save(updatedUserData);
        return mapToDTO(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);
        user.setStatus(false); // Baja lógica
        userRepository.save(user);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    private UserResponseDTO mapToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setDocumentNumber(user.getDocumentNumber());
        dto.setStatus(user.getStatus());

        // Mapear Tipo de Documento al CatalogResponseDTO
        if (user.getDocumentType() != null) {
            CatalogResponseDTO docTypeDto = new CatalogResponseDTO();
            docTypeDto.setId(user.getDocumentType().getId());
            docTypeDto.setName(user.getDocumentType().getName());
            dto.setDocumentType(docTypeDto);
        }

        // Mapear Rol al CatalogResponseDTO
        if (user.getRole() != null) {
            CatalogResponseDTO roleDto = new CatalogResponseDTO();
            roleDto.setId(user.getRole().getId());
            roleDto.setName(user.getRole().getName());
            dto.setRole(roleDto);
        }

        return dto;
    }
}
