package com.example.citylibrary.config;

import com.example.citylibrary.user.UserRepository;
import com.example.citylibrary.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found or you do not have permission to view this");
        }

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map(role ->
                        new SimpleGrantedAuthority(role.getName())).toList());

    }
}
