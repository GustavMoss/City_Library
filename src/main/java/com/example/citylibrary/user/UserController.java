package com.example.citylibrary.user;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.loan.Loans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // See a users active loans
    @GetMapping("/{userId}")
    public List<Loans> getUserLoansByUserId(@PathVariable Long userId) {
        return userService.getLoansById(userId);
    }

    // create/register new user
    @PostMapping
    public ResponseEntity<Users> postNewUser(@RequestBody Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }


    // update user info
    @PutMapping("/{userId}")
    public Users updateUser(@PathVariable Long userId, @RequestBody Users user) {
        return userService.updateUserById(userId, user);
    }

    // these might need to be seperate? Or maybe the above ones?

    // user get their own loans
    @GetMapping("/{userId}/loans")
    public List<Loans> getUserLoansById(@PathVariable Long userId) {
        // this is essentially the same endpoint as the above get, just with different address. Might want to involving some authentication though?
        return userService.getLoansById(userId);
    }

    // see return date for active loans, pretty much the same as above?
    @GetMapping("/{userId}/loans/active")
    public List<Loans> getActiveUserLoansById(@PathVariable Long userId) {
        List<Loans> userLoans;
        List<Loans> activeLoans = new ArrayList<>();
        userLoans = userService.getLoansById(userId);

        for (Loans loan : userLoans) {
            if (loan.getReturned_date() == null) {
                activeLoans.add(loan);
            }
        }

        return activeLoans;
    }

    // loan a book, this might just call the loan methods/controller?
    @PostMapping("/{userId}/new-loan")
    public Loans createNewLoan(@PathVariable Long userId, @RequestBody Loans loan) {
        // not sure about this one.
        return null;
    }
}
