package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // public Student save(Student student);
    Optional<Student> findByEmail(String email);
}
