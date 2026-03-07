package com.example.demo.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginStudentDto;
import com.example.demo.dto.RegisterStudentDto;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;

@Service
public class AuthenticationService {

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        StudentRepository StudentRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.studentRepository = StudentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student signup(RegisterStudentDto input) {
        Student student = new Student();
        student.setFullName(input.getFullName());
        student.setEmail(input.getEmail());
        student.setPassword(passwordEncoder.encode(input.getPassword()));
        return studentRepository.save(student);
    }

    public Student authenticate(LoginStudentDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return studentRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}