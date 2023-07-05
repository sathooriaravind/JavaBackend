package com.example.minor1.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter@Setter@Builder@NoArgsConstructor@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String txnId;

    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    // while issuing book, fine is zero
    // while returning book, fine might be zero or more based on on-time return
    private Integer fine;

    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties("transactionList")
    private Student student;

    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties("transactionList")
    private Book book;

    @ManyToOne
    @JoinColumn
    private Admin admin;


}
