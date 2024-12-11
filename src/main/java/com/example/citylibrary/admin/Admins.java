package com.example.citylibrary.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Admins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long admin_id;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @NotEmpty(message = "Username cannot be empty")
    private String password;

    private String role;

}
