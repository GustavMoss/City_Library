package com.example.citylibrary.book;

import com.example.citylibrary.exceptions.LibBadRequest;
import com.example.citylibrary.exceptions.LibBookIsOnLoan;
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
            book.get().setAuthor(newBooks.getAuthor());
            book.get().setAvailable(newBooks.isAvailable());
            book.get().setPublication_year(newBooks.getPublication_year());
            return bookRepository.save(book.get());
        } else {
            return null;
        }
    }

    public void changeAvailability(Long id) throws LibBadRequest {
        Optional<Books> book = bookRepository.findById(id);
        if (book.isPresent()) {
            book.get().setAvailable(!book.get().isAvailable());
            bookRepository.save(book.get());
        } else {
            throw new LibBadRequest("Could not find book with id " + id);
        }
    }

    public boolean deleteBook(Long id) {
        Optional<Books> bookToDelete = bookRepository.findById(id);
        if (bookToDelete.isPresent()) {
            if (!bookToDelete.get().isAvailable()) {
                throw new LibBookIsOnLoan("The book is on loan and cannot be deleted");
            } else {
                bookRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }


}
