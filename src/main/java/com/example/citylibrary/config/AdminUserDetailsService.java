package com.example.citylibrary.config;

import com.example.citylibrary.admin.AdminPrincipal;
import com.example.citylibrary.admin.AdminRepository;
import com.example.citylibrary.admin.Admins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admins admin = adminRepo.findByUsername(username);

        if (admin == null) {
            System.out.println("Admin not found");
            throw new UsernameNotFoundException("Admin not found");
        }
        return new AdminPrincipal(admin);
    }
}
