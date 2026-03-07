package com.example.demo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {

    private String token;

    private long expiresIn;
    
}
