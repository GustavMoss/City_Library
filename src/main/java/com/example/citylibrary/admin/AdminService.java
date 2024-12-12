package com.example.citylibrary.admin;


import com.example.citylibrary.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


    private final JWTService jwtService;
    private final AuthenticationManager authManager;

    @Autowired
    public AdminService(JWTService jwtService, AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    // TODO: add functionality to create new admins/librarians?


    public String verify(Admins admin) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));

        if (auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            return jwtService.generateToken(admin.getUsername());

        } else {
            return "failed to verify admin";
        }

    }
}
