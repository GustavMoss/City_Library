package com.example.citylibrary.user;


import lombok.Data;

@Data
public class UserDTO {
    Long user_id;
    String first_name;
    String last_name;
    String email;
    String member_number;
}
