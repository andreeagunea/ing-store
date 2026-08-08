package com.ing.andreea.teastore.dto;

import com.ing.andreea.teastore.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleUpdate {

    @NotNull(message = "Role cannot be null")
    private UserRole role;
}
