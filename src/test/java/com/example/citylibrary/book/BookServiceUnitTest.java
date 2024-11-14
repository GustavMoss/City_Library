package com.example.citylibrary.book;

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
class BookServiceUnitTest {
    @Autowired
    private BookService bookService;

    @Test
    void getAllBooks() {
       List<Books> result = bookService.getAllBooks();

       assertNotNull(result);
       assertEquals(10, result.size());
    }

    @Test
    void addBook() {

        Books book = Books.builder().book_id(11L).title("Gardens of the Moon").publication_year(1999).available(true).build();

        bookService.addBook(book);

        assertNotNull(book);
        assertEquals(11, book.getBook_id());
        assertEquals("Gardens of the Moon", book.getTitle());
    }

    @Test
    void getBookById() {

        Books result = bookService.getBookById(1L).orElse(null);
        assertEquals(1L, result.getBook_id());
        assertEquals("Pippi Långstrump", result.getTitle());
    }

    @Test
    void updateBook() {
        Books updatedBook = bookService.getBookById(1L).orElse(null);

        updatedBook.setTitle("Inte Pippi");

        Books result = bookService.updateBook(updatedBook, 1L);

        assertEquals("Inte Pippi", result.getTitle());
        assertEquals(1L, result.getBook_id());
    }

    @Test
    void deleteBook() {
        boolean result = bookService.deleteBook(1L);

        assertTrue(result);
    }
}