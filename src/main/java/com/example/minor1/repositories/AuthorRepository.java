package com.example.minor1.repositories;

import com.example.minor1.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorRepository extends JpaRepository<Author,Integer> {

    @Query
    Author findByEmail(String email);

}
