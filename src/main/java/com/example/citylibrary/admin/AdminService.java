package com.example.citylibrary.admin;


import com.example.citylibrary.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class AdminService {


    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authManager;


    // TODO: with the auth here, I'm getting a stack overflow error for some reason, I guess it gets stuck in a loop? Recursive call?
    public String verify(Admins admin) {
        /*Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword()));

        if (auth.isAuthenticated()) {
            return jwtService.generateToken(admin.getUsername());
        } else {
            return "failed to verify admin";
        }*/

        return jwtService.generateToken(admin.getUsername());
    }
}
