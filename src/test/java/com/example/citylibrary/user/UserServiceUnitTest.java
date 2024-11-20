package com.example.citylibrary.user;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.example.citylibrary.loan.LoanService;
import com.example.citylibrary.loan.Loans;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
class UserServiceUnitTest {

    // ********************************
    // Byggda av Simon Jönsson
    // ********************************

    @Autowired
    private UserService userService;
    private LoanService loanService;

    @Test
    void createNewUser() {
        Users user = Users.builder()
                .user_id(11L)
                .first_name("Mange")
                .last_name("Baker")
                .email("mangebaker@gmail.com")
                .member_number("M20230006")
                .build();

        userService.createNewUser(user);

        assertNotNull(user);

        assertEquals(11L, user.getUser_id());
        assertEquals("Mange", user.getFirst_name());
        assertEquals("Baker", user.getLast_name());
        assertEquals("mangebaker@gmail.com", user.getEmail());
        assertEquals("M20230006", user.getMember_number());
    }

    @Test
    void getLoansById() {
        Users user1 = userService.getUserById(1L).orElse(null);
        assertNotNull(user1);

        List<Loans> result = userService.getLoansByUserId(user1.getUser_id());

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getUserById() {
        Users user1 = userService.getUserById(1L).orElse(null);

        assertNotNull(user1);
        assertEquals(1L, user1.getUser_id());
        assertEquals("Anna", user1.getFirst_name());
        assertEquals("Andersson", user1.getLast_name());
        assertEquals("anna.andersson@email.com", user1.getEmail());
    }

    @Test
    void updateUserById() {
        Users updatedUser = userService.getUserById(1L).orElse(null);

        updatedUser.setFirst_name("Monika");
        updatedUser.setLast_name("Bengtsson");
        updatedUser.setEmail("monikabengtsson@gmail.com");

        Users result = userService.updateUserById(1L, updatedUser);

        assertEquals(1L, result.getUser_id());
        assertEquals("Monika", result.getFirst_name());
        assertEquals("Bengtsson", result.getLast_name());
        assertEquals("monikabengtsson@gmail.com", result.getEmail());
    }
}