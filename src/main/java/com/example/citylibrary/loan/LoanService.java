package com.example.citylibrary.loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.example.citylibrary.user.UserService;
import com.example.citylibrary.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.book.Books;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;


    @Autowired
    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    public List<Loans> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loans> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

    public Loans createLoan(Long bookId , Long userId) {

        Loans loan = new Loans();
        
        Books book = bookService.getBookById(bookId).orElse(null);
        loan.setBook_Id(book);
        Users user = userService.getUserById(bookId).orElse(null);
        loan.setUser_id(user);
        
        loan.setLoan_date(LocalDate.now());
        loan.setDue_date(LocalDate.now().plusMonths(1));
        loan.setReturned_date(null);

        return loanRepository.save(loan);
    }

    public Loans updateLoan(Long id, Loans newLoan) {
        Optional<Loans> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            loan.get().setBook_Id(newLoan.getBook_Id());
            loan.get().setDue_date(newLoan.getDue_date());
            loan.get().setLoan_date(newLoan.getLoan_date());
            loan.get().setLoan_id(newLoan.getLoan_id());
            loan.get().setReturned_date(newLoan.getReturned_date());
            loan.get().setUser_id(newLoan.getUser_id());
            return loanRepository.save(loan.get());
        } else {
            return null;
        }
    }

    public Loans addReturnedDate(Long id) {
        Optional<Loans> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            loan.get().setReturned_date(LocalDate.now());
            return loanRepository.save(loan.get());
        } else {
            return null;
        }
    }

    public void deleteLoan(Long id) {
        Optional<Loans> loanToDelete = loanRepository.findById(id);
        if (loanToDelete.isPresent()) {
            loanRepository.deleteById(loanToDelete.get().getLoan_id());
        } else {
            // Skapa en ExceptionHandler här för Loan som
            // inte existerar ???
        }
    }
}
