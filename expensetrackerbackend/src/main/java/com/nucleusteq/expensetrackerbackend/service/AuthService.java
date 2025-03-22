package com.nucleusteq.expensetrackerbackend.service;

import com.nucleusteq.expensetrackerbackend.dto.RegisterRequest;
import com.nucleusteq.expensetrackerbackend.model.User;
import com.nucleusteq.expensetrackerbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterRequest request) {
        // Check company email
        if (!request.getEmail().endsWith("@company.com")) {
            return "Only company emails are allowed.";
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered.";
        }

        // Save user in database
        User user = new User(null, request.getName(), request.getEmail(), 
                             passwordEncoder.encode(request.getPassword()), 
                             request.getRole(), request.getDepartment());
        userRepository.save(user);

        return "User registered successfully!";
    }
}
