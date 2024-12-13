package com.example.citylibrary.user;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<Users, UserDTO> {
    @Override
    public UserDTO apply(Users users) {
        return new UserDTO(
                users.getUser_id(),
                users.getFirst_name(),
                users.getLast_name(),
                users.getEmail(),
                users.getMember_number()
        );
    }
}
