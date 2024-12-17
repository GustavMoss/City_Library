package com.example.citylibrary.loan;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.book.Books;
import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;
    private final UserDTOMapper userDTOMapper;


    @Autowired
    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService, UserDTOMapper userDTOMapper) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.userDTOMapper = userDTOMapper;
    }

    public List<Loans> getAllLoans() {
        return loanRepository.findAll();
    }

    public ResponseEntity<List<Loans>> getAllActiveLoans() {
        List<Loans> allLoans = loanRepository.findAll();

        return new ResponseEntity<>(allLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public Optional<Loans> getLoanById(Long id) {

        // TODO: this seems to work, so do something similar to getAllLoans and getAllActiveLoans above.
        Optional<Loans> loan = loanRepository.findById(id);

        UserDTO sanitizedUser = userDTOMapper.toDTO(loan.get().getUser());

        loan.get().setUser(userDTOMapper.toUsers(sanitizedUser));

        return loan;
    }

    public Loans createLoan(Long bookId , Long userId) throws LibBadRequest {

        Loans loan = new Loans();

        Optional<Books> book = bookService.getBookById(bookId);

        if (book.isPresent()) {
            if (!book.get().isAvailable()) {
                throw new LibBadRequest("Book is not available");
            }
            loan.setBook(book.get());
            bookService.changeAvailability(book.get().getBook_id());
        } else {
            throw new LibBadRequest("book not found");
        }

        Optional<UserDTO> userDTO = userService.getUserById(userId);
        Users user = userDTOMapper.toUsers(userDTO.get());

        if (user != null) {
            loan.setUser(user);
        } else {
            throw new LibBadRequest("user not found");
        }

        loan.setLoan_date(LocalDate.now());
        loan.setDue_date(LocalDate.now().plusMonths(1));
        loan.setReturned_date(null);

        return loanRepository.save(loan);
    }

    public Loans updateLoan(Long id, Loans newLoan) throws LibBadRequest {
        Optional<Loans> loan = loanRepository.findById(id);
        if (loan.isPresent()) {
            loan.get().setDue_date(newLoan.getDue_date());
            loan.get().setLoan_date(newLoan.getLoan_date());
            return loanRepository.save(loan.get());
        } else {
            throw new LibBadRequest("could not find loan with id: " + id);
        }
    }
    
    public Loans addReturnedDate(Long id) throws LibBadRequest {
        Optional<Loans> loan = loanRepository.findById(id);

        if (loan.isPresent()) {
            Optional<Books> book = bookService.getBookById(loan.get().getBook().getBook_id());
            if (book.isPresent()) {
                book.get().setAvailable(true);
            } else {
                throw new LibBadRequest("book not found");
            }
            loan.get().setReturned_date(LocalDate.now());
            return loanRepository.save(loan.get());
        } else {
            throw new LibBadRequest("could not find loan with id: " + id);
        }
    }

    public void deleteLoan(Long id) throws LibBadRequest {
        Optional<Loans> loanToDelete = loanRepository.findById(id);
        if (loanToDelete.isPresent()) {
            loanRepository.deleteById(loanToDelete.get().getLoan_id());
        } else {
            throw new LibBadRequest("Could not find loan with id " + id);
        }
    }
}
