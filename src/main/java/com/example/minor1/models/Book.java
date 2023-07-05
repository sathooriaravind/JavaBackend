package com.example.minor1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.EnumType.STRING;


@Getter@Setter@Builder@NoArgsConstructor@AllArgsConstructor
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Enumerated(value = STRING)
    private Genre genre;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @ManyToOne
    @JoinColumn // Adds authorId as foreign key in book table -- remember this annotation
    @JsonIgnoreProperties({"bookList"}) // ignores value of bookList from author table and prints null
    private Author author;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties({"bookList"})
    private Student student;

    /*
    @ManyToOne
    @JoinColumn
    private Admin admin;
     */

    @OneToMany(mappedBy = "book")
    private List<Transaction> transactionList;

}
