package com.example.citylibrary.admin;

import lombok.Data;

// currently no end-point returns an unsanitized admin object, thus this is not used. Same goes for the mapper
@Data
public class AdminDTO {
    private Long id;
    private String username;
}
