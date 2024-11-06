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
    private Long loanId;

    //private Long bookId;

    private Long userId;

    private LocalDate loanDate;

    private LocalDate dueDate;

    private LocalDate returnedDate;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Books bookId;

}
