package com.Hotel_System.Mucyo.service;

import com.Hotel_System.Mucyo.dto.AuthResponse;
import com.Hotel_System.Mucyo.dto.LoginRequest;
import com.Hotel_System.Mucyo.dto.RegisterRequest;
import com.Hotel_System.Mucyo.exception.AuthenticationException;
import com.Hotel_System.Mucyo.model.Role;
import com.Hotel_System.Mucyo.model.User;
import com.Hotel_System.Mucyo.repository.UserRepository;
import com.Hotel_System.Mucyo.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final NotificationService notificationService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email {} already exists", request.getEmail());
            throw new AuthenticationException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        
        User savedUser = userRepository.save(user);
        
        // Send welcome email
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getName(), user.getRole());
            log.info("Welcome email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", user.getEmail(), e);
            // Don't throw the exception, as the user is already registered
        }

        // Create welcome notification
        notificationService.createNotification(
            savedUser.getId(),
            "Welcome to Hotel System! Your account has been created successfully.",
            "WELCOME"
        );

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getRole().name(), user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Attempting to login user with email: {}", request.getEmail());
        
        try {
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found with email: {}", request.getEmail());
                    return new AuthenticationException("User not found");
                });

            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtService.generateToken(user);
            log.info("User logged in successfully: {}", user.getEmail());
            return new AuthResponse(token, user.getRole().name(), user.getName(), user.getEmail());
            
        } catch (BadCredentialsException e) {
            log.warn("Login failed: Invalid credentials for user: {}", request.getEmail());
            throw new AuthenticationException("Invalid email or password");
        } catch (Exception e) {
            log.error("Login failed: Unexpected error for user: {}", request.getEmail(), e);
            throw new AuthenticationException("Login failed: " + e.getMessage());
        }
    }
} 