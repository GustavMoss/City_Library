package com.example.citylibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.
                authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/users").permitAll() // this should not be open to everyone probably, make a new endpoint specifically for register probably.
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // TODO: remove before production, only here to get h2-console to work
                .csrf(AbstractHttpConfigurer::disable) // have to disable csrf for the h2-console to work, get forbidden otherwise. TODO: Remove before production
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());

        return http.build();
    }

    // this will compare passwords as far as I understand. The daoauth provider is needed to get the info from database
    // and then we set the passwordencoder and set a userdetailsservice that we've customized to fit our need. In this to get the email instead of username even though it's called by username in the userdetails interface, so a bit confusing.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    // init the password encoder, built into spring security.
    // Used to encode password with bcrypt, the strength is the number of rounds
    // seems very simple to implement.
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder(12);
    }
}
