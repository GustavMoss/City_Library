package com.example.citylibrary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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


    private final CustomUserDetailsService userDetailsService;
    private final AdminUserDetailsService adminUserDetailsService;
    private final JwtFilter jwtFilter;
    private final JWTAdminFilter jwtAdminFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, AdminUserDetailsService adminUserDetailsService, JwtFilter jwtFilter, JWTAdminFilter jwtAdminFilter) {
        this.userDetailsService = userDetailsService;
        this.adminUserDetailsService = adminUserDetailsService;
        this.jwtFilter = jwtFilter;
        this.jwtAdminFilter = jwtAdminFilter;
    }

    // TODO: the access control is a bit busted, most/many end-points are not set up correctly.
    //  Might have to set up 3 matchers? One for admin specific end-points like creating user and so forth and then user specific endpoints like fetching my loans. and then a more general matcher for fetching all the books or something
    // also admins wont have access to the /users endpoints. Which to be fair might be fine. MIght need to specify the specific admin endpoints under /admin/something something though could put them under the admin folder I guess and break them out of user/loan/books then set correct authorities depending on librarian/admin. I guess admin might have access to everythin librarian does, but not vice versa
    // TODO: need to fix the stackoverflow error as well. Although at the moment it is not being used and it still seem to (somewhat) work. Might not need that bit? Although I guess that would mean that verification method is useless and security is worse.
    // TODO: accessing some of the user endpoints seems a bit weird, getting Resolved [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException: Method parameter 'userId': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: "login"] errors. I guess it's expecting a userId and a GET request?
    // might need to try testing that a bit more. 
    @Bean
    @Order(1)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        http.
                securityMatcher("/users/**")
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/users/register", "/users/login").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/books/id").permitAll()
                                .requestMatchers("/books/").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable) //
                .logout(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
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
                                .requestMatchers("/admin/login").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/admin/create-new-user").hasAuthority("ADMIN")
                                .anyRequest().authenticated())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(adminUserDetailsService)
                .logout(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAdminFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // TODO: is this needed?
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
