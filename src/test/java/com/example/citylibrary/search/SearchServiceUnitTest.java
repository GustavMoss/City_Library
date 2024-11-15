package com.example.citylibrary.search;

import com.example.citylibrary.book.Books;
import jakarta.transaction.Transactional;
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
class SearchServiceUnitTest {

    @Autowired
    private SearchService searchService;

    @Test
    void findByTitle() {
        List<Books> result = searchService.findByTitle("Pippi");
        assertEquals(1, result.size()); //1 = hur många objekt som finns i listan
        assertEquals("Pippi Långstrump", result.get(0).getTitle()); //0 = första positionen i listan
    }

    @Test
    void findByAuthor() {
        List<Books> result = searchService.findByAuthor("Astrid");
        assertEquals(2, result.size());
        assertEquals("Pippi Långstrump", result.get(0).getTitle());
        assertEquals("Bröderna Lejonhjärta", result.get(1).getTitle());
    }

    @Test
    @Transactional
    void findByGenre() {
        List<Books> result = searchService.findByGenre("Horror");
        assertEquals(2, result.size());
        assertEquals("The Shining", result.get(0).getTitle());
        assertEquals("Pet Sematary", result.get(1).getTitle());
    }

    @Test
    void checkAvailability() {
        boolean isAvailable = searchService.checkAvailability("Pippi");
        assertTrue(isAvailable);

        boolean isNotAvailable = searchService.checkAvailability("Bröderna");
        assertFalse(isNotAvailable);
    }
}