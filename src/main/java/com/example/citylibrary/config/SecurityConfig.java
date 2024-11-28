package com.example.citylibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
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
                {

                    try {
                        auth
                                .requestMatchers("/users/register", "/users/login").permitAll() // might need to change these as we keep working. If nothing else add new ones for like admin and user and such
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated().and().headers().frameOptions().sameOrigin(); // and().headers().frameOptions().sameOrigin() is to get h2-console to work. Have to set the X-Frame something header otherwise the browser will complain. most of them are deprecated and I have to wrap in a try/catch since the .headers throws an error. there is probably an easier or prettier way than this.
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })

                .csrf(AbstractHttpConfigurer::disable) // have to disable csrf for the h2-console to work, get forbidden otherwise.
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

    // need to get a hold of this, probably to tell it to generate and return tokens eventually? This talks to the AuthenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
       return config.getAuthenticationManager();
    }
}
