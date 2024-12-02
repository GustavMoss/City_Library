package com.example.citylibrary.loan;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.book.Books;
import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.user.UserService;
import com.example.citylibrary.user.Users;
import com.example.citylibrary.user.UsersDTO;
import com.example.citylibrary.user.UsersDTOMapper;
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
    private final UsersDTOMapper usersDTOMapper;


    @Autowired
    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService, UsersDTOMapper usersDTOMapper) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.usersDTOMapper = usersDTOMapper;
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

        Optional<UsersDTO> user = userService.getUserById(userId);

        if (user.isPresent()) {
            // this doesnt throw an error here, but I think it wont run when we hit the end-point. The issue is that setUser_id need an object since its mapped by the ORM and just converting the DTO to a normal users object seems a little finicky but I think it might be the solution
            // or building a half-assed Users object with the data from the dto as long as it has the ID. Another option could be to have a different service for more internal fetching of data, so the create loan endpoint would use a service that returns a proper User object behind the scenes
            // but then return a DTO or similiarly sanitized response
            loan.setUser_id((Users) user.stream().map(u -> new Users()));
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
            Optional<Books> book = bookService.getBookById(loan.get().getBook_Id().getBook_id());
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
