package com.example.citylibrary.user;

import com.example.citylibrary.loan.LoanService;
import com.example.citylibrary.loan.Loans;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final LoanService loanService;

    @Autowired
    public UserController(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    // Get user by id
    @GetMapping("/{userId}")
    public Users getUserById(@PathVariable Long userId){
        return userService.getUserById(userId).orElse(null);

    // create/register new user
    @PostMapping
    public ResponseEntity<Users> postNewUser(@RequestBody @Valid Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    // update user info
    @PutMapping("/{userId}")
        public Users updateUser(@PathVariable Long userId, @RequestBody @Valid Users user) {
    }

    // return all of a users loans both inactive and active
    @GetMapping("/{userId}/loans")
    public List<Loans> getAllUserLoansById(@PathVariable Long userId) {
        return userService.getLoansByUserId(userId);
    }

    // might be able to use this one endpoint for both the "Bibliotekarier - låntagare endpoint and the besökare - låntagarfunktioner" both are for seeing active loans. It will also let us get the return dates.
    // or do I need a seperate endpoint for that?
    @GetMapping("/{userId}/loans/active")
    public List<Loans> getActiveUserLoansById(@PathVariable Long userId) {
        List<Loans> userLoans = userService.getLoansByUserId(userId);

        return userLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList());
    }

    // loan a book by calling the loanservice and using its methods
    @PostMapping("/{userId}/new-loan")
    public Loans createNewLoan(@PathVariable Long userId, @RequestParam Long bookId) {

        return loanService.createLoan(bookId, userId);

    }
}
