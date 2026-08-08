package com.ing.andreea.teastore.service;

import com.ing.andreea.teastore.dto.RoleUpdate;
import com.ing.andreea.teastore.dto.UserResponse;
import com.ing.andreea.teastore.model.entity.UserEntity;
import com.ing.andreea.teastore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getRole()
                ))
                .collect(Collectors.toList());
    }

    public UserResponse updateUserRole(Long id, RoleUpdate request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setRole(request.getRole());
        userRepository.save(user);

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
