package com.example.citylibrary.loan;

import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.user.UserRepository;
import com.example.citylibrary.user.UserService;
import com.example.citylibrary.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepo;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public LoanService(LoanRepository loanRepo, UserService userService, UserRepository userRepository) {
        this.loanRepo = loanRepo;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // creates a new loan in the db with userId, bookId and dates
    public Loans createNewLoan(Long userId, Long bookId) {

        Loans newLoan = new Loans();

        // fetch the user
        Optional<Users> currentUser = userRepository.findById(userId);

        if (currentUser.isPresent()) {
            newLoan.setUser(currentUser.get());
        } else {
            throw new LibBadRequest("Could not find user with id " + userId);
        }

        newLoan.setBook_id(bookId);
        newLoan.setReturned_date(null);
        newLoan.setLoan_date(LocalDate.now());
        newLoan.setDue_date(LocalDate.now().plusMonths(1));

        return loanRepo.save(newLoan);
    }
}
