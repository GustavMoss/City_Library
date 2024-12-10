package com.example.citylibrary.loan;


import com.example.citylibrary.book.Books;
import com.example.citylibrary.user.UserDTO;
import com.example.citylibrary.user.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loan_id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book_Id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user_id;

    @NotNull(message = "loan date cannot be null")
    private LocalDate loan_date;

    @NotNull(message = "due date cannot be null")
    private LocalDate due_date;

    private LocalDate returned_date;

}
