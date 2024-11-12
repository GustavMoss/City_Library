package com.example.citylibrary.user;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.loan.Loans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public List<Loans> getUserLoansByUserId(@PathVariable Long id) {
        return userService.getLoansById(id);
    }

    // create/register new user
    @PostMapping
    public ResponseEntity<Users> postNewUser(@RequestBody Users user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.CREATED);
    }


    // update user info
    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users user) {
        return userService.updateUserById(id, user);
    }
}
