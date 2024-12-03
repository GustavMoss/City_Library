package com.example.citylibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private CustomUserDetailsService userDetailsService;
    private AdminUserDetailsService adminUserDetailsService;
    private JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, AdminUserDetailsService adminUserDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.adminUserDetailsService = adminUserDetailsService;
        this.jwtFilter = jwtFilter;
    }

    // TODO: the access control is a bit busted, most/many end-points are not accessibly by anyone since we haven't defined them here.
    @Bean
    @Order(1)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http.
                securityMatcher("/users/**")
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/users/register", "/users/login").permitAll() // might need to change these as we keep working. If nothing else add new ones for like admin and user and such
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/books/id").permitAll()
                                .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // TODO: remove before production, only here to get h2-console to work
                .csrf(AbstractHttpConfigurer::disable) // have to disable csrf for the h2-console to work, get forbidden otherwise. TODO: Remove before production
                .logout(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // save nothing in the session since we are using JWT to auth with, at least that's the idea, seems to break login though
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        http.
                securityMatcher("/admin/**")
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/admin/login").permitAll() // might need to change these as we keep working. If nothing else add new ones for like admin and user and such
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // TODO: remove before production, only here to get h2-console to work
                .csrf(AbstractHttpConfigurer::disable) // have to disable csrf for the h2-console to work, get forbidden otherwise. TODO: Remove before production
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(adminUserDetailsService)
                .logout(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // save nothing in the session since we are using JWT to auth with, at least that's the idea, seems to break login though
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // TODO: is this needed?
    // this will compare passwords as far as I understand. The daoauth provider is needed to get the info from database
    // and then we set the passwordencoder and set a userdetailsservice that we've customized to fit our need. In this to get the email instead of username even though it's called by username in the userdetails interface, so a bit confusing.
    /*@Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }*/

    // need to get a hold of this, probably to tell it to generate and return tokens eventually? This talks to the AuthenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // need to declare this here since it will use no encoder otherwise since I commented out the above authprovider
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
