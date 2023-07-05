package com.example.minor1.repositories;

import com.example.minor1.models.Book;
import com.example.minor1.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

        List<Book> findByGenre(Genre genre);

        // understand the naming convention to get author name from book table
        List<Book> findByAuthor_name(String authorName);

        List<Book> findByName(String bookName);
}
