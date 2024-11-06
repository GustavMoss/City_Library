package com.example.citylibrary.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Books> getAllBooks() {
        return bookRepository.findAll();
    }

    public Books addBook(Books books) {
        return bookRepository.save(books);
    }

    public Optional<Books> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Books updateBook(Books newBooks, Long id) {
        Optional<Books> book = bookRepository.findById(id);
        if (book.isPresent()) {
            book.get().setTitle(newBooks.getTitle());
            book.get().setAuthorId(newBooks.getAuthorId());
            book.get().setPublicationYear(newBooks.getPublicationYear());
            return bookRepository.save(book.get());
        } else {
            return null;
        }
    }


}
