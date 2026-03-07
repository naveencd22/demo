package com.example.demo.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.service.StudentService;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void addStudent(Student student) {
        try {
            studentRepository.save(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void addStudent1() {
        // Example list of people's names
        List<String> names = Arrays.asList("John", "Sara", "Mark", "Sara", "Chris", "Paula");

        Map<String, Long> collect2 = names.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String val = "JohnJ";
        Map<Character, Long> collect1 = val.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(": " + collect1);


        List<Integer> num = Arrays.asList(1,2,3,4,5,6);
        Map<Boolean, List<Integer>> collect = num.stream().collect(Collectors.partitioningBy(i -> i % 2 == 0));
        System.out.println("Names List1: " + collect);
//        names.stream().collect(Collectors.groupingBy(str-> str.charAt(0)));
        System.out.println("Names List1: " + names);
        // Collecting into a List
        List<String> nameList = names.stream().collect(Collectors.toList());

        System.out.println("Names List: " + nameList);
        // Grouping names by the first letter
        Map<Character, List<String>> namesByFirstLetter = names.stream()
                .collect(Collectors.groupingBy(name -> name.charAt(0)));

        System.out.println("Names Grouped by First Letter: " + namesByFirstLetter);
        // Joining names into a single string separated by commas
        String allNames = names.stream().collect(Collectors.joining(", "));
        System.out.println("All Names Joined: " + allNames);
        // Counting the distinct names
        long distinctNameCount = names.stream().distinct().count();
        System.out.println("Distinct Names Count: " + distinctNameCount);

    }


    @Override
    public List<Student> allStudents() {
       return studentRepository.findAll();
    }

    // public static void main(String[] args) {
    //     StudentServiceImpl ss =new StudentServiceImpl();
    //     ss.addStudent();
    // }
}
