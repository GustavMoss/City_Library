package com.example.citylibrary.search;

import jakarta.transaction.Transactional;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
@AutoConfigureMockMvc
@Transactional
class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void searchByTitle() throws Exception {
        mockMvc.perform(get("/search/title")
                .param("title", "pippi lång")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Pippi Långstrump"));
    }

    @Test
    void searchByAuthor() throws Exception {
        mockMvc.perform(get("/search/author")
                .param("author", "Astrid Lindg")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Pippi Långstrump"))
                .andExpect(jsonPath("$[1].title").value("Bröderna Lejonhjärta"));
    }

    @Test
    void searchByGenre() throws Exception {
        mockMvc.perform(get("/search/genre")
                .param("genre", "Horror")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Shining"))
                .andExpect(jsonPath("$[1].title").value("Pet Sematary"));
    }

    @Test
    void checkAvailability() throws Exception {
        mockMvc.perform(get("/search/availability")
                .param("title", "Pippi Långstrump")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("The book is available"));

        mockMvc.perform(get("/search/availability")
                .param("title", "Bröderna Lejonhjärta")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The book is not available"));
    }
}