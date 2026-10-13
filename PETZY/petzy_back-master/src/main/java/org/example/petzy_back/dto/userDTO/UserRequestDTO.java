package org.example.petzy_back.dto.userDTO;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Long documentTypeId;
    private String documentNumber;
    private String address;
    private Long roleId;
}
