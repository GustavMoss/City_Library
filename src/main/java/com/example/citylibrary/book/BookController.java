package com.example.citylibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<Books> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public Books addBook(@RequestBody Books books) {
        return bookService.addBook(books);
    }

    @PutMapping("/{id}")
    public Books updateBook(@PathVariable Long id, @RequestBody Books books) {
        return bookService.updateBook(books, id);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
