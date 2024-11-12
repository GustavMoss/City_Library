package com.example.citylibrary.author;

import com.example.citylibrary.book.Books;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    public Authors(Long author_id, String first_name, String last_name, LocalDate birth_date) {
        this.author_id = author_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;

    }
}
