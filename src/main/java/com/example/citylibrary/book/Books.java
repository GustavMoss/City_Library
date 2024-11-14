package com.example.citylibrary.book;

import com.example.citylibrary.author.Authors;
import com.example.citylibrary.genre.Genres;
import com.example.citylibrary.loan.Loans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    private String title;

    private int publication_year;

    private boolean available;

    @OneToMany(mappedBy = "book_Id", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Loans> loans;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Authors author;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genres> genres;

}
