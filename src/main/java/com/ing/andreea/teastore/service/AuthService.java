package com.ing.andreea.teastore.service;

import com.ing.andreea.teastore.dto.AuthResponse;
import com.ing.andreea.teastore.dto.LoginRequest;
import com.ing.andreea.teastore.model.entity.UserEntity;
import com.ing.andreea.teastore.model.enums.UserRole;
import com.ing.andreea.teastore.repository.UserRepository;
import com.ing.andreea.teastore.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdmin() {
        return args -> {
            if (userRepository.findByUsername("adminUser").isEmpty()) {
                UserEntity admin = UserEntity.builder()
                        .username("adminUser")
                        .password(passwordEncoder.encode("admin123"))
                        .role(UserRole.ADMIN)
                        .build();

                userRepository.save(admin);
                System.out.println("Admin user created");
            } else {
                System.out.println("Admin user already exists");
            }
        };
    }

    public AuthResponse register(LoginRequest request) {
        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}

