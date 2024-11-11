package com.example.citylibrary.user;

import com.example.citylibrary.loan.Loans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String first_name;

    private String last_name;

    private String email;

    private String member_number;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Loans> loans;

    //Search by title
    //Search by author
    //Search by ISBN

    //Check if book is available

    //Check active loans and return date

}
