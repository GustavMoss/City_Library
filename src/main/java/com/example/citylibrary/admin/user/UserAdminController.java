package com.example.citylibrary.admin.user;

import com.example.citylibrary.loan.Loans;
import com.example.citylibrary.user.UserService;
import com.example.citylibrary.user.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;


    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;

    }

    // create/register new user
    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Users> postNewUser(@RequestBody @Valid Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }

    // update existing user
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody @Valid Users user) {
        Users updatedUser = userService.updateUserById(userId, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}/active-loans")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'LIBRARIAN')")
    public ResponseEntity<List<Loans>> getActiveUserLoansById(@PathVariable Long userId) {
        List<Loans> userLoans = userService.getLoansByUserId(userId);
        return new ResponseEntity<>(userLoans.stream()
                .filter(loan -> loan.getReturned_date() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
