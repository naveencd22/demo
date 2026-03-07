package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginStudentDto;
import com.example.demo.dto.RegisterStudentDto;
import com.example.demo.model.Student;
import com.example.demo.service.impl.AuthenticationService;
import com.example.demo.service.impl.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Student> register(@RequestBody RegisterStudentDto registerStudentDto) {
        Student registeredStudent = authenticationService.signup(registerStudentDto);

        return ResponseEntity.ok(registeredStudent);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginStudentDto loginStudentDto) {
        Student authenticatedStudent = authenticationService.authenticate(loginStudentDto);

        String jwtToken = jwtService.generateToken(authenticatedStudent);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    
}
