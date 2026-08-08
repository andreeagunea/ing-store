package com.ing.andreea.teastore.controller;

import com.ing.andreea.teastore.dto.RoleUpdate;
import com.ing.andreea.teastore.dto.UserResponse;
import com.ing.andreea.teastore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        logger.info("Retrieving all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdate request) {
        logger.info("Updating role for user with id {}", id);
        return ResponseEntity.ok(userService.updateUserRole(id, request));
    }
}
