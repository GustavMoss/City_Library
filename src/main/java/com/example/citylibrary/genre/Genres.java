package com.example.citylibrary.genre;

import com.example.citylibrary.book.Books;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genre_id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private List<Books> books;

}
