package com.example.citylibrary.book;

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
class BookControllerIntegrationTest {

    // ********************************
    // Byggda av Martin Leo
    // ********************************

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    void getBookById() throws Exception {

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book_id").value(1))
                .andExpect(jsonPath("$.title").value("Pippi Långstrump"));
    }

    @Test
    void addBook() throws Exception {
        String jsonBook = "{\"book_id\": 11, \"title\": \"House of Leaves\", \"publication_year\": 2000 }";

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBook))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book_id").value(11))
                .andExpect(jsonPath("$.title").value("House of Leaves"))
                .andExpect(jsonPath("$.publication_year").value(2000));
    }

    @Test
    void updateBook() throws Exception {
        String jsonBook = "{\"title\": \"House of Leaves\"}";

        mockMvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBook))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.book_id").value(1))
                .andExpect(jsonPath("$.title").value("House of Leaves"));
    }

    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isBadRequest());
    }
}