package com.example.citylibrary.user;

import com.example.citylibrary.loan.LoanService;
import com.example.citylibrary.loan.Loans;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
    // FIXME: should user have access to this? I'd assume we'd need something like this to say print out a users info on their userpage or something, maybe as long as we strictly control the data being sent it's fine?
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Long userId){
        Optional<UserDTO> user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // FIXME: moved to admin, but might want to keep it here as well? Users should be able to create an account by themselves??
    // create/register new user
    @PostMapping("/register")
    public ResponseEntity<Users> postNewUser(@RequestBody @Valid Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    // FIXME: move this from here to a more general end-point?`(/login instead of /users/login) To be fair the end result would most likely be the same, the URL would be the only difference?
    // login end-point
    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }

    // FIXME: moved to admin, delete this when safe
    // update user info
   /* @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody @Valid Users user) {
        Users updatedUser = userService.updateUserById(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }*/

    // return all of a users loans both inactive and active
    @GetMapping("/{userId}/loans")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<List<Loans>> getAllUserLoansById(@PathVariable Long userId) {
        List<Loans> userLoans = userService.getLoansByUserId(userId);
        return new ResponseEntity<>(userLoans, HttpStatus.OK);
    }

    // FIXME: moved to admin as well, does a user need this? Probably right? to check their own active loans
    // returns active loans by user id
    @GetMapping("/{userId}/loans/active")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<List<Loans>> getActiveUserLoansById(@PathVariable Long userId) {
        List<Loans> userLoans = userService.getLoansByUserId(userId);
        return new ResponseEntity<>(userLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    // loan a book by calling the loanservice and using its methods
    @PostMapping("/{userId}/new-loan")
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<Loans> createNewLoan(@PathVariable Long userId, @RequestParam Long bookId) {
        Loans newLoan = loanService.createLoan(bookId, userId);
        return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
    }
}
