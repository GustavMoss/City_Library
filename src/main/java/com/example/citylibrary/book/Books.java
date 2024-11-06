package com.example.citylibrary.book;

import com.example.citylibrary.person.Authors;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    private String title;

    private int publicationYear;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Authors authorId;

    private boolean available;
}
