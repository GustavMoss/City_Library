package com.example.citylibrary.user;

import com.example.citylibrary.loan.LoanService;
import com.example.citylibrary.loan.Loans;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    @GetMapping("/get-user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Optional<UserDTO>> getUser(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserDTO> user = userService.getUserByEmail(userDetails.getUsername());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // FIXME: moved to admin, but users should be able so sign up by themselves so keep this here as well
    //  or should they just both call this endpoint?

    // create/register new user
    @PostMapping("/register")
    public ResponseEntity<Users> postNewUser(@RequestBody @Valid Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    // TODO: move this to it's own auth controller?
    // login end-point
    @PostMapping("/login")
    public String login(@RequestBody Users user) {
        return userService.verify(user);
    }

    // FIXME: moved to admin, delete this when safe. Although users should be able to update their own info
    //  so might want to keep this here.

    // update user info
   /* @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody @Valid Users user) {
        Users updatedUser = userService.updateUserById(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }*/

    // return all of a users loans both inactive and active
    @GetMapping("/loans")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<Loans>> getAllUserLoans(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserDTO> user = userService.getUserByEmail(userDetails.getUsername());
        List<Loans> userLoans = userService.getLoansByUserId(user.get().user_id);

        return new ResponseEntity<>(userLoans, HttpStatus.OK);
    }

    // returns active loans by user id
    @GetMapping("/loans/active")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<List<Loans>> getActiveUserLoans(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserDTO> user = userService.getUserByEmail(userDetails.getUsername());
        List<Loans> userLoans = userService.getLoansByUserId(user.get().getUser_id());

        return new ResponseEntity<>(userLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    // loan a book by calling the loanservice and using its methods
    @PostMapping("/new-loan")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Loans> createNewLoan(Authentication authentication, @RequestParam Long bookId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<UserDTO> user = userService.getUserByEmail(userDetails.getUsername());
        Loans newLoan = loanService.createLoan(bookId, user.get().getUser_id());

        return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
    }
}
