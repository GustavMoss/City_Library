package com.example.citylibrary.book;

import com.example.citylibrary.exceptions.LibBookIsOnLoan;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks() {
        List<Books> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Books>> getBookById(@PathVariable Long id) {
        Optional<Books> book = bookService.getBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Books> addBook(@RequestBody @Valid Books books) {
        Books book = bookService.addBook(books);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable @Valid Long id, @RequestBody Books books) {
        Books updatedBook = bookService.updateBook(books, id);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            boolean isDeleted = bookService.deleteBook(id);
            if (isDeleted) {
                return ResponseEntity.ok("Book with id " + id + " was successfully deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + id + " not found.");
            }
        } catch (LibBookIsOnLoan ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }
}
