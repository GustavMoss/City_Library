package com.example.citylibrary.loan;

import java.time.LocalDate;

import com.example.citylibrary.book.Books;
import com.example.citylibrary.user.Users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
