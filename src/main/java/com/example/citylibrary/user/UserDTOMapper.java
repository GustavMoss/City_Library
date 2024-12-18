package com.example.citylibrary.user;

import org.springframework.stereotype.Service;


@Service
public class UserDTOMapper {

    public UserDTO toDTO(Users user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(user.getUser_id());
        userDTO.setFirst_name(user.getFirst_name());
        userDTO.setLast_name(user.getLast_name());
        userDTO.setEmail(user.getEmail());
        userDTO.setMember_number(user.getMember_number());

        return userDTO;
    }

    public Users toUsers(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        Users users = new Users();
        users.setUser_id(userDTO.getUser_id());
        users.setFirst_name(userDTO.getFirst_name());
        users.setLast_name(userDTO.getLast_name());
        users.setEmail(userDTO.getEmail());
        users.setMember_number(userDTO.getMember_number());

        return users;
    }

}
