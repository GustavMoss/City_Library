package com.example.citylibrary.admin.books;

import com.example.citylibrary.book.BookService;
import com.example.citylibrary.book.Books;
import com.example.citylibrary.exceptions.LibBookIsOnLoan;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin/books")
public class BooksAdminController {

    final BookService bookService;

    @Autowired
    public BooksAdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Optional<Books>> getBookById(@PathVariable Long bookId) {
        Optional<Books> book = bookService.getBookById(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/add-book")
    public ResponseEntity<Books> addNewBook(@RequestBody @Valid Books book) {
        Books newBook = bookService.addBook(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/update-book/{bookId}")
    public ResponseEntity<Books> updateBook(@PathVariable @Valid Long bookId, @RequestBody Books books) {
        Books updatedBook = bookService.updateBook(books, bookId);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}/delete-book")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        try {
            boolean isDeleted = bookService.deleteBook(bookId);
            if (isDeleted) {
                return ResponseEntity.ok("Book with id " + bookId + " was successfully deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with id " + bookId + " not found.");
            }
        } catch (LibBookIsOnLoan ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
        }
    }

}
