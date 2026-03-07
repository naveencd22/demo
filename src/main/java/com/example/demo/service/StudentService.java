package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Student;


public interface StudentService {

    public void addStudent(Student student);
    public List<Student> allStudents();
}
