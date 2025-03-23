package com.nucleusteq.expensetrackerbackend.service;

import com.nucleusteq.expensetrackerbackend.dto.AuthRequest;
import com.nucleusteq.expensetrackerbackend.dto.RegisterRequest;
import com.nucleusteq.expensetrackerbackend.model.User;
import com.nucleusteq.expensetrackerbackend.model.Role;
import com.nucleusteq.expensetrackerbackend.repository.UserRepository;
import com.nucleusteq.expensetrackerbackend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // **Login Method**
    public String login(AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());

        if (userOptional.isPresent() && passwordEncoder.matches(authRequest.getPassword(), userOptional.get().getPassword())) {
            return jwtUtil.generateToken(authRequest.getEmail());
        }
        throw new RuntimeException("Invalid email or password");
    }

    // **Registration Method**
    public String registerUser(RegisterRequest request) {
        if (!request.getEmail().endsWith("@company.com")) {
            return "Only company emails are allowed.";
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered.";
        }

        Role userRole;
        try {
            userRole = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "Invalid role provided.";
        }

        User user = new User(null, request.getName(), request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                userRole, request.getDepartment());
        userRepository.save(user);

        return "User registered successfully!";
    }
}
