package com.example.citylibrary.user;

import com.example.citylibrary.loan.Loans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be at least 2 characters long")
    private String first_name;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be at least 2 characters long")
    private String last_name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid email")
    private String email;

    @NotBlank(message = "Member-number cannot be empty")
    private String member_number;

    @OneToMany(mappedBy = "user_id")
    @JsonIgnore
    private List<Loans> loans;

    //Search by title
    //Search by author
    //Search by ISBN

    //Check if book is available

    //Check active loans and return date

}
