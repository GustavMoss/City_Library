package com.example.citylibrary.loan;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
class LoanServiceTest {

    // ********************************
    // Byggda av Mohammad Khaleqi
    // ********************************


    @Autowired
    private LoanService loanService;

    @BeforeEach
    void setUp() {
    }


    @Test
    void getAllLoans() {

        List<Loans> result = loanService.getAllLoans();


        assertEquals(6, result.size());

    }

    @Test
    void getLoanById() {
        Loans result = loanService.getLoanById(1L).orElse(null);
        assertEquals(1, result.getLoan_id());
    }

    @Test
    void createLoan() {
        Loans result = loanService.createLoan(1L,4L);

        assertEquals(11, result.getLoan_id());
        assertEquals(LocalDate.now(),result.getLoan_date());
        assertEquals(LocalDate.now().plusMonths(1),result.getDue_date());


        assertEquals(1, result.getBook_Id().getBook_id());
        assertEquals(4, result.getUser_id().getUser_id());
        /*assertEquals(5, result.setLoan_date(LocalDate.now()),setDue_date(LocalDate.now()));*/

    }

    @Test
    void updateLoan() {
        Loans updateLoan = loanService.getLoanById(1L).orElse(null);
        updateLoan.setLoan_date(LocalDate.of(2024,05,23));
        Loans result = loanService.updateLoan(1L,updateLoan);
        assertEquals(1, result.getLoan_id());



    }

    @Test
    void addReturnedDate() {
        Loans result = loanService.addReturnedDate(3L);
        assertEquals(3, result.getLoan_id());
        assertEquals(LocalDate.now(),result.getReturned_date());
    }

    @Test
    void deleteLoan() {
        loanService.deleteLoan(2L);
        Loans result =loanService.getLoanById(2L).orElse(null);
        assertNull(result);
    }
}