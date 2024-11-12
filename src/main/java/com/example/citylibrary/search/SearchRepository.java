package com.example.citylibrary.search;

import com.example.citylibrary.book.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository extends JpaRepository<Books, Long> {

}
