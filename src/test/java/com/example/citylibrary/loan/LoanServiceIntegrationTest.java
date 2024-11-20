package com.example.citylibrary.loan;

import com.example.citylibrary.book.BookRepository;
import com.example.citylibrary.book.Books;
import com.example.citylibrary.user.UserRepository;
import com.example.citylibrary.user.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LoanServiceIntegrationTest {

    @Autowired
    private LoanRepository loansRepository;

    @Autowired
    private BookRepository booksRepository;

    @Autowired
    private UserRepository usersRepository;

    private Users testUser;
    private Books testBook;

    @BeforeEach
    void setUp() {

        testBook = new Books();
        testBook.setTitle("Test Book");
        testBook = booksRepository.save(testBook);


        testUser = new Users();
        testUser.setFirst_name("Test User");
        testUser.setLast_name("Svensson");
        testUser.setMember_number("abc");
        testUser.setEmail("jek@gmail.com");
        testUser = usersRepository.save(testUser);

    }

    @Test
    void shouldSaveAndFetchLoan() {

        Loans loan = new Loans();
        loan.setBook_Id(testBook);
        loan.setUser_id(testUser);
        loan.setLoan_date(LocalDate.now());
        loan.setDue_date(LocalDate.now().plusDays(14));

        Loans savedLoan = loansRepository.save(loan);


        assertThat(savedLoan.getLoan_id()).isNotNull();
        assertThat(savedLoan.getBook_Id()).isEqualTo(testBook);
        assertThat(savedLoan.getUser_id()).isEqualTo(testUser);
        assertThat(savedLoan.getDue_date()).isEqualTo(LocalDate.now().plusDays(14));


        Loans fetchedLoan = loansRepository.findById(savedLoan.getLoan_id()).orElse(null);
        assertThat(fetchedLoan).isNotNull();
        assertThat(fetchedLoan.getLoan_date()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldHandleLoanRelationships() {

        Loans loan = new Loans();
        loan.setBook_Id(testBook);
        loan.setUser_id(testUser);
        loan.setLoan_date(LocalDate.now());
        loan.setDue_date(LocalDate.now().plusDays(7));

        loansRepository.save(loan);


        Loans fetchedLoan = loansRepository.findAll().get(0);
        assertThat(fetchedLoan.getBook_Id().getTitle()).isEqualTo("Bröderna Lejonhjärta");
        assertThat(fetchedLoan.getUser_id().getFirst_name()).isEqualTo("Anna");
    }
}
