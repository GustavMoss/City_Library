package com.example.citylibrary.user;

import com.example.citylibrary.loan.Loans;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void createNewUser() {
        Users user = Users.builder()
                .user_id(11L)
                .first_name("Mange")
                .last_name("Baker")
                .email("mangebaker@gmail.com")
                .build();

        userService.createNewUser(user);

        assertNotNull(user);

        assertEquals(11L, user.getUser_id());
        assertEquals("Mange", user.getFirst_name());
        assertEquals("Baker", user.getLast_name());
        assertEquals("mangebaker@gmail.com", user.getEmail());
    }

    @Test
    void getLoansById() {

        List<Loans> result = userService.getLoansById().get().getUser_id().getLoans();


        /*
        * Books result = bookService.getBookById(1L).orElse(null);
        assertEquals(1L, result.getBook_id());
        assertEquals("Pippi Långstrump", result.getTitle());
        * */
    }

    @Test
    void getUserById() {
    }

    @Test
    void updateUserById() {
    }
}