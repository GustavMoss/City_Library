package com.example.citylibrary.author;

import com.example.citylibrary.book.Books;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Authors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long author_id;

    private String first_name;

    private String last_name;

    private LocalDate birth_date;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<Books> books;

}
