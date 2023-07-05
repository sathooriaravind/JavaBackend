package com.example.minor1.controllers;

import com.example.minor1.dtos.CreateBookRequest;
import com.example.minor1.models.Book;
import com.example.minor1.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BookController {

    private static Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService bookService;

    @PostMapping("/book")
    public void createBook(@RequestBody @Valid CreateBookRequest createBookRequest){
        /* First author should be created then book bcz book table has author as foreign key.
        If done in reverse order, again we need to update book table after author gets created,
        so first way is efficient.
         */

        bookService.createOrUpdate(createBookRequest.toBook());

    }

    // This below get Api can be used for different types of searches on books
    // localhost:8080/book?key=authorName&value=Aravind
    // localhost:8080/book?key=genre&value=PROGRAMMING
    @GetMapping("/book")
    public List<Book> getBooks(@RequestParam("key") String key,
                               @RequestParam("value") String value) throws Exception {
        return bookService.find(key,value);
    }

    /*
    @GetMapping("/book")
    public List<Book> getBooks(@RequestBody @Valid SearchBookRequest searchBookRequest) throws Exception {
        return bookService.find(searchBookRequest.getKey(),searchBookRequest.getValue());
    }

     */
}
