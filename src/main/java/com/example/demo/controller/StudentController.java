package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearer-key")
public class StudentController {

    @Autowired
    StudentService studentService;

    @PostMapping("/addStudents")
    public void createStudent(@RequestBody Student student) {
        studentService.addStudent(student);

    }

    @GetMapping("/me")
    public ResponseEntity<Student> authenticatedUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Student currentUser = (Student) authentication.getPrincipal();

            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/")
    public ResponseEntity<List<Student>> allUsers() {
        List <Student> students = studentService.allStudents();

        return ResponseEntity.ok(students);
    }
    
}
