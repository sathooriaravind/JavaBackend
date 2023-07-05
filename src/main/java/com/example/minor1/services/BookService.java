package com.example.minor1.services;


import com.example.minor1.models.Author;
import com.example.minor1.models.Book;
import com.example.minor1.models.Genre;
import com.example.minor1.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private static Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    AuthorService authorService;
    @Autowired
    BookRepository bookRepository;

    public void createOrUpdate(Book book) {
        // so first author is saved or checking if already exists
        Author authorSaved = authorService.getOrCreate(book.getAuthor());

        book.setAuthor(authorSaved);

        // saving book with updated author details with id
        bookRepository.save(book);
    }

    public List<Book> find(String key, String value) throws Exception {
        /*
            One way is by writing switch case and directing to the correct findBy method
         */
        switch(key){
            case "id" :
            {
                Optional<Book> book = bookRepository.findById(Integer.parseInt(value));
                if(book.isPresent())
                    return Arrays.asList(book.get());
                else
                    return new ArrayList<>();
            }
            case "genre" :
                return bookRepository.findByGenre(Genre.valueOf(value));
            case "authorName" :
                return bookRepository.findByAuthor_name(value);
            case "bookName" :
                return bookRepository.findByName(value);
            default:
                throw new Exception("search key not valid: {}" + key );
        }
    }

}
