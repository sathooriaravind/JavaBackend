package com.example.minor1.repositories;

import com.example.minor1.models.Book;
import com.example.minor1.models.Student;
import com.example.minor1.models.Transaction;
import com.example.minor1.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    Transaction findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(Student student,
                                                                       Book book,
                                                                       TransactionType transactionType);

    Transaction findByTxnId(String txnId);
}
