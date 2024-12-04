package com.example.citylibrary.user;

import com.example.citylibrary.loan.LoanService;
import com.example.citylibrary.loan.Loans;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/{member_number}/{username}")
    public ResponseEntity<Optional<Users>> getUserByMemberNumberAndUsername
    (@RequestParam String member_number, @RequestParam String username){
        Optional<Users> user = userService.getUserByMemberNumber(member_number);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    // create/register new user
    @PostMapping
    public ResponseEntity<Users> postNewUser(@RequestBody @Valid Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    // update user info
    @PutMapping("/{member_number}/{username}")
    public ResponseEntity<Users> updateUser(@RequestParam String member_number, @RequestBody @Valid Users user) {
        Users updatedUser = userService.updateUserByMemberNumber(member_number,  user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // return all of a users loans both inactive and active
    @GetMapping("/{member_number}/{username}/loans")
    public ResponseEntity<List<Loans>> getAllUserLoans(@RequestParam Long member_number) {
        List<Loans> userLoans = userService.getLoansByMemberNumber(member_number);
        return new ResponseEntity<>(userLoans, HttpStatus.OK);
    }

    // returns active loans by user id
    @GetMapping("/{member_number}/{username}/loans/active")
    public ResponseEntity<List<Loans>> getActiveUserLoans(@RequestParam Long member_number) {
        List<Loans> userLoans = userService.getLoansByMemberNumber(member_number);
        /*List<Loans> activeUserLoans = userLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList());*/
        return new ResponseEntity<>(userLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    // loan a book by calling the loanservice and using its methods
    @PostMapping("/{member_number}/{username}/new-loan")
    public ResponseEntity<Loans> createNewLoan(@PathVariable Long member_number,  @RequestParam Long bookId) {
        Loans newLoan = loanService.createLoan(bookId,member_number);
        return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
    }
}
