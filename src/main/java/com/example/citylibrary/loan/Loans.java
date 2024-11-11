package com.example.citylibrary.loan;

import com.example.citylibrary.user.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loan_id;

   // private Long user_id;

    private Long book_id;

    private LocalDate loan_date;

    private LocalDate due_date;

    private LocalDate returned_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    /*@OneToOne
    @JoinColumn(name = "book_id")
    private Books bookId;*/

}
