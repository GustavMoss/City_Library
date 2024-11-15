package com.example.citylibrary.loan;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.book.Books;
import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.user.UserService;
import com.example.citylibrary.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public Loans createLoan(Long bookId , Long userId) throws LibBadRequest {

        Loans loan = new Loans();

        // TODO: take another look at the error handling, not printing out the message. Might need to use try/catch?
        Optional<Books> book = bookService.getBookById(bookId);

        if (book.isPresent()) {
            if (!book.get().isAvailable()) {
                throw new LibBadRequest("Book is not available");
            }
            loan.setBook_Id(book.get());
            bookService.changeAvailability(book.get().getBook_id());
        } else {
            throw new LibBadRequest("book not found");
        }

        Optional<Users> user = userService.getUserById(userId);

        if (user.isPresent()) {
            loan.setUser_id(user.get());
        } else {
            throw new LibBadRequest("user not found");
        }

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

    // TODO: change availability of the book once it's returned, also return a proper error or something? MIght be done in the controller though
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
