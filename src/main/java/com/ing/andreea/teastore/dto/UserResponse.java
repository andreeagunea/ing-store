package com.ing.andreea.teastore.dto;

import com.ing.andreea.teastore.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private UserRole role;
}
