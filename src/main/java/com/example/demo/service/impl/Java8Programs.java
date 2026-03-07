package com.example.demo.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Java8Programs {
    public static void main(String[] args) {
        // 1. Lambda Expression Example
        Runnable runnable = () -> System.out.println("Hello, Lambda!");
        new Thread(runnable).start();

        // 2. Reverse String Using Java 8
        reverseStringUsingJava8("Hello, World!");

        // 3. Reverse String Using Java 8 Streams
        duplicateElementsInStringJava8("Hello, World!");

        // 4. duplicateElementsInListUsingJava8
        findDuplicateElementsInListUsingJava8();
    }

    private static void reverseStringUsingJava8(String input) {
        String reversed = new StringBuilder(input).reverse().toString();
        System.out.println("Reversed String: " + reversed);
    }

    private static void duplicateElementsInStringJava8(String input) {
        String duplicated = input.chars()
                .mapToObj(c -> String.valueOf((char) c))
                .reduce("", (a, b) -> a + b + b);
        System.out.println("Duplicated Elements: " + duplicated);
    }

    private static void findDuplicateElementsInListUsingJava8() {
        List<String> list = Arrays.asList("apple", "banana", "apple", "orange", "banana");
        
        List<String> duplicates = list.stream()
                .filter(i -> list.indexOf(i) != list.lastIndexOf(i))
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Duplicate Elements in List: " + duplicates);
        
        Set<String> duplicates1 = list.stream().filter(n -> Collections.frequency(list, n) > 1).collect(Collectors.toSet());
        System.out.println(duplicates1);
    }
}
