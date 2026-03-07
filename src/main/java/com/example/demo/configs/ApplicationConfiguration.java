package com.example.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.repository.StudentRepository;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class ApplicationConfiguration {

    
    private final StudentRepository studentRepository;

    public ApplicationConfiguration(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // This method is used by the DaoAuthenticationProvider to load user details during authentication
    @Bean
    UserDetailsService userDetailsService() {
        return username -> studentRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // This method is used to encode passwords before storing them in the database and to verify passwords during authentication
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // This method is used to provide the AuthenticationManager bean, which is responsible for processing authentication requests
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // This method is used to configure the authentication provider, which is responsible for authenticating users based on the user details service and password encoder
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("Demo API").version("1.0"));
    }
}
