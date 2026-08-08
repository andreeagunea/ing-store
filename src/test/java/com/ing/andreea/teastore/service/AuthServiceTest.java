package com.ing.andreea.teastore.service;

import com.ing.andreea.teastore.dto.AuthResponse;
import com.ing.andreea.teastore.dto.LoginRequest;
import com.ing.andreea.teastore.model.entity.UserEntity;
import com.ing.andreea.teastore.model.enums.UserRole;
import com.ing.andreea.teastore.repository.UserRepository;
import com.ing.andreea.teastore.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("andreea");
        loginRequest.setPassword("password123");

        userEntity = UserEntity.builder()
                .id(1L)
                .username("andreea")
                .password("encodedPassword123")
                .role(UserRole.USER)
                .build();
    }

    @Test
    void registerUserSuccessfully() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("mocked-jwt-token");

        AuthResponse result = authService.register(loginRequest);

        assertNotNull(result);
        assertEquals("mocked-jwt-token", result.getToken());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(jwtService, times(1)).generateToken(any(UserEntity.class));
    }

    @Test
    void registerUserWithUserRoleSuccessfully() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword123");
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn("mocked-jwt-token");

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            assertEquals(UserRole.USER, savedUser.getRole());
            assertEquals("andreea", savedUser.getUsername());
            assertEquals("encodedPassword123", savedUser.getPassword());
            return savedUser;
        });

        authService.register(loginRequest);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void loginSuccessfully() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername("andreea")).thenReturn(Optional.of(userEntity));
        when(jwtService.generateToken(userEntity)).thenReturn("mocked-jwt-token");

        AuthResponse result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("mocked-jwt-token", result.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByUsername("andreea");
        verify(jwtService, times(1)).generateToken(userEntity);
    }

    @Test
    void throwExceptionWhenCredentialsAreWrong() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByUsername(any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void throwExceptionWhenUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByUsername("andreea")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        verify(userRepository, times(1)).findByUsername("andreea");
        verify(jwtService, never()).generateToken(any());
    }
}
