package com.example.citylibrary.loan;

import com.example.citylibrary.book.Books;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.awt.print.Book;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loan_id;

    private Long user_id;

    private Long book_id;

    private LocalDate loan_date;

    private LocalDate due_date;

    private LocalDate returned_date;

    /*@OneToOne
    @JoinColumn(name = "book_id")
    private Books bookId;*/

}
