package com.example.citylibrary.user;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UsersDTOMapper implements Function<Users, UsersDTO> {
    @Override
    public UsersDTO apply(Users users) {
        return new UsersDTO(
                users.getUser_id(),
                users.getFirst_name(),
                users.getLast_name(),
                users.getEmail(),
                users.getMember_number()
                // might need to add authorities later as well something like this: getAuthorities().stream().map(r -> r.getAuthorities()).collect(Collectors.toList())
        );
    }
}
