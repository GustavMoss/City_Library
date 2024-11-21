package com.example.citylibrary.loan;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
@AutoConfigureMockMvc
@Transactional
class LoanControllerTest {

    // ********************************
    // Byggda av Mohammad Khaleqi
    // ********************************

    @Autowired
    private MockMvc mockMvc;
    @Test
    void getAllLoans() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(6));
    }

    @Test
    void getLoanById() throws Exception{
        mockMvc.perform(get("/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loan_id")
                        .value(1));
    }

    @Test
    void createLoan() throws Exception{
        String jsonLoan = "{" +
                "\"user_id\": 1," +
                "\"book_id\": 6," +
                "}";
        mockMvc.perform(post("/loans")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("bookId","6")
                .param("userId","1")
                .content(jsonLoan)).andExpect(status().isCreated()).andExpect(jsonPath("$.loan_id")
                .value(11));
    }

    @Test
    void updateLoan() throws Exception {
        String jsonLoan = "{" +
                "\"loan_date\": \"2023-02-15\"," +
                "\"due_date\": \"2025-05-29\"" +
                "}";
        mockMvc.perform(put("/loans/1").contentType(MediaType.APPLICATION_JSON)
                .content(jsonLoan)).andExpect(status().isOk())
                .andExpect(jsonPath("$.loan_id").value(1))
                .andExpect((jsonPath("$.due_date").value("2025-05-29")))
                .andExpect(jsonPath("$.loan_date").value("2023-02-15"));
    }

    @Test
    void addReturnedDate() throws Exception{
        mockMvc.perform(put("/loans/return/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.returned_date")
                        .value(LocalDate.now().toString()));
    }

    @Test
    void deleteLoan() throws Exception{
        mockMvc.perform(delete("/loans/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/loans/1")).andExpect(status().isOk());

    }
}