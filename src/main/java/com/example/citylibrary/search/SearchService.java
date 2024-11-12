package com.example.citylibrary.search;

import com.example.citylibrary.author.Authors;
import com.example.citylibrary.book.Books;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepository;

    public List<Books> findByTitle(String title) {
        List<Books> allBooks = searchRepository.findAll();
        return allBooks.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Books> findByAuthor(String author) {
        List<Books> allBooks = searchRepository.findAll();
        return allBooks.stream()
                .filter(book -> {
                    Authors bookAuthor = book.getAuthor();
                    return bookAuthor.getFirstName().toLowerCase().contains(author.toLowerCase())
                            || bookAuthor.getLastName().toLowerCase().contains(author.toLowerCase());
                })
                .collect(Collectors.toList());
    }

    public List<Books> findByGenre(String genre) {
        List<Books> allBooks = searchRepository.findAll();
        return allBooks.stream()
                .filter(book -> book.getGenres().stream()
                        .anyMatch(g -> g.getName().toLowerCase().contains(genre.toLowerCase())))
                .collect(Collectors.toList());
    }

    public boolean checkAvailability(String title) {
        List<Books> allBooks = searchRepository.findAll();
        for (Books book : allBooks) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase()) && book.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}