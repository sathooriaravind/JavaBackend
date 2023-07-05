package com.example.minor1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
@Entity
public class Admin {

    // useful in spring security but not much now

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @OneToOne
    @JoinColumn
    @JsonIgnoreProperties("student")
    private SecuredUser securedUser;

    /*
    @OneToMany(mappedBy = "admin")
    private List<Book> bookList;
     */

    /*
    @OneToMany(mappedBy = "admin")
    private List<Student> studentList;
     */

    @OneToMany(mappedBy = "admin")
    @JsonIgnoreProperties("admin")
    private List<Transaction> transactionList;


}
