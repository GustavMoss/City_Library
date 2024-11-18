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
    public List<Books> getAllBooks() {
        return bookService.getAllBooks();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Books>> getBookById(@PathVariable Long id) {
        Optional<Books> book = bookService.getBookById(id);

        if (book.isPresent()) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Books addBook(@RequestBody @Valid Books books) {
        return bookService.addBook(books);
    }

    @PutMapping("/{id}")
    public Books updateBook(@PathVariable @Valid Long id, @RequestBody Books books) {
        return bookService.updateBook(books, id);
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
