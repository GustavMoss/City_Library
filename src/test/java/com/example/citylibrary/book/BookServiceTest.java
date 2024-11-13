package com.example.citylibrary.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void getAllBooks() {
    }

    @Test
    void addBook() {
    }

    @Test
    void getBookById() {
        Books bookOne = new Books();
        bookOne.setBook_id(5L);

        when(bookRepository.findById(5L)).thenReturn(Optional.of(bookOne));

        Books result = bookService.getBookById(5L).orElse(null);
        assertEquals(5L, result.getBook_id());
        verify(bookRepository).findById(5L);
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }
}