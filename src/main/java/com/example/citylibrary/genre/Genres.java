package com.example.citylibrary.genre;

import com.example.citylibrary.book.Books;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Genres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long genre_id;

    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonIgnore
    private List<Books> books;

}
