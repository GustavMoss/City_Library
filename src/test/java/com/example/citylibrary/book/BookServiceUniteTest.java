package com.example.citylibrary.book;



import com.example.citylibrary.book.BookRepository;
import com.example.citylibrary.book.Books;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:data.sql")
class BookServiceTest {



  @Autowired
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initierar mock-objekten
    }

    @Test
    void shouldReturnAllBooks() {

        List<Books> result = bookService.getAllBooks();
        assertNotNull(result);
        assertEquals(10, result.size());

       /* List<Books> books = new ArrayList<>();
        books.add(new Books(1L, "Book1", "Author1", true, 2020));
        books.add(new Books(2L, "Book2", "Author2", false, 2018));

        when(bookRepository.findAll()).thenReturn(books);


        List<Books> result = bookService.getAllBooks();


        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Book1");
        verify(bookRepository, times(1)).findAll();*/
    }

    @Test
    void shouldAddBook() {

        Books book = Books.builder().book_id(11L).title("Book1").publication_year(1996).available(true).build();



        Books result = bookService.addBook(book);


        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Book1");

    }


}
