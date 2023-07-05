package com.example.minor1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter@Setter@Builder@NoArgsConstructor@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    // unique identifier for particular author because two authors can share same name
    @Column(nullable = false,unique = true)
    private String email;

    @CreationTimestamp
    private Date createdOn;


    // mappedBy adds back reference by telling us to look through
    // column named "authorId" in Book table
    @OneToMany(mappedBy = "author")
    @JsonIgnoreProperties("author")
    private List<Book> bookList;
    // Because of back reference, below two queries runs
    // select * from author where id =1 and
    // select * from book where author=1

    /*
        For querying from author table, i want book list not to be null but since the column
        List<Book> bookList will be assigned null in the author table without back referencing,
        we will get null when queried to get books list of some author. So here we are using
        back referencing by adding "mappedBy = "author"" annotation value on top of bookList.
        Now, when we query for book list by author, then hibernate goes to "author" column in book
        table to get list of books of particular author. this
     */


}
