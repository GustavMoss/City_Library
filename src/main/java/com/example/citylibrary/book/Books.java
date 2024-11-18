package com.example.citylibrary.book;

import com.example.citylibrary.author.Authors;
import com.example.citylibrary.genre.Genres;
import com.example.citylibrary.loan.Loans;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Name must be between 5 and 100 characters long")
    private String title;

    @Min(value = 0, message = "Publication year cannot be a negative number")
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
