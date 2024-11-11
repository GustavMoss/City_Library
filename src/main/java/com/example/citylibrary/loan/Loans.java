package com.example.citylibrary.loan;

import com.example.citylibrary.book.Books;
import com.example.citylibrary.user.Users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loan_id;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Books book_Id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user_id;
    
    private LocalDate loan_date;

    private LocalDate due_date;

    private LocalDate returned_date;


}
