package com.example.citylibrary.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void postNewUser() throws Exception {
        String jsonUser = "{" +
                "\"user_id\": 6," +
                "\"first_name\": \"Fabian\"," +
                "\"last_name\": \"Liljegren\"," +
                " \"email\": \"fabian.liljegren@hotmail.com\"," +
                " \"member_number\": \"M20230007\" }";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_id").value(11))
                .andExpect(jsonPath("$.first_name").value("Fabian"))
                .andExpect(jsonPath("$.last_name").value("Liljegren"))
                .andExpect(jsonPath("$.email").value("fabian.liljegren@hotmail.com"))
                .andExpect(jsonPath("$.member_number").value("M20230007"))
                ;
    }

    @Test
    void updateUser() throws Exception {
        String jsonUser = "{" +
                "\"first_name\": \"Molgan\"," +
                "\"last_name\": \"Persson\"," +
                " \"email\": \"molgan.persson@hotmail.com\", " +
                " \"member_number\": \"M20230001\" " +
                "}";

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id").value(1))
                .andExpect(jsonPath("$.first_name").value("Molgan"))
                .andExpect(jsonPath("$.last_name").value("Persson"))
                .andExpect(jsonPath("$.email").value("molgan.persson@hotmail.com"))
                .andExpect(jsonPath("$.member_number").value("M20230001"))
        ;
    }

    @Test
    void getUserLoansById() throws Exception {
        mockMvc.perform(get("/users/1/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getActiveUserLoansById() throws Exception {
        mockMvc.perform(get("/users/1/loans/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void createNewLoan() throws Exception {
        String jsonLoan = "{" +
                "\"user_id\": 1," +
                "\"book_id\": 6," +
                "\"loan_date\": 2023-02-15," +
                "\"due_date\": 2025-05-29," +
                "\"returned_date\": null," +
                "}";

        mockMvc.perform(post("/users/1/new-loan")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("bookId", "6")
                        .content(jsonLoan))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loan_id").value(11))
                .andExpect(jsonPath("$.user_id.user_id").value(1))
                .andExpect(jsonPath("$.book_Id.book_id").value(6))

                ;
    }
}
