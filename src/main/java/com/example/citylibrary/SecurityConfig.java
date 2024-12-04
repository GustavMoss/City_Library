package com.example.citylibrary;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;



    @Configuration
    @EnableWebSecurity
    public class SecurityConfig {
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http.authorizeHttpRequests(auth -> auth
                            .requestMatchers("/user/**").permitAll()
                            .anyRequest().authenticated())
                    .formLogin(Customizer.withDefaults())
                    .logout(Customizer.withDefaults())
                 .csrf(csrf -> csrf.disable());
            return http.build();



        }


    }

