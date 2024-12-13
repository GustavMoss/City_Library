package com.example.citylibrary.user;

public record UserDTO(
        Long user_id,
        String first_name,
        String last_name,
        String email,
        String member_number
) {
}
