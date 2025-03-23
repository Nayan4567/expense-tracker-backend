package com.nucleusteq.expensetrackerbackend.controller;

import com.nucleusteq.expensetrackerbackend.dto.AuthRequest;
import com.nucleusteq.expensetrackerbackend.dto.RegisterRequest;
import com.nucleusteq.expensetrackerbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // **User Registration**
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    // **User Login**
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = authService.login(authRequest);
        return ResponseEntity.ok("Bearer " + token);
    }
}
