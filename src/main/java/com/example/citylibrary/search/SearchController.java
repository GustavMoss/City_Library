package com.example.citylibrary.search;

import com.example.citylibrary.book.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/title")
    public ResponseEntity<List<Books>> searchByTitle(@RequestParam String title) {
        List<Books> books = searchService.findByTitle(title);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Books>> searchByAuthor(@RequestParam String author) {
        List<Books> books = searchService.findByAuthor(author);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/genre")
    public ResponseEntity<List<Books>> searchByGenre(@RequestParam String genre) {
        List<Books> books = searchService.findByGenre(genre);
        if (books.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/availability")
    public ResponseEntity<String> checkAvailability(@RequestParam String title) {
        boolean available = searchService.checkAvailability(title);
        if (!available) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The book is not available");
        }
        return ResponseEntity.ok("The book is available");
    }

}
