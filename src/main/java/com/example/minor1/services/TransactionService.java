package com.example.minor1.services;

import com.example.minor1.dtos.InitiateTransactionRequest;
import com.example.minor1.models.*;
import com.example.minor1.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BookService bookService;

    @Autowired
    StudentService studentService;

    @Autowired
    AdminService adminService;

    @Value("${student.allowed.max-books}")
    Integer maxBooksAllowed;

    @Value("${student.allowed.duration}")
    Integer maxDuration;

    public String initiateTxn(InitiateTransactionRequest initiateTransactionRequest,int adminId) throws Exception {

        if(initiateTransactionRequest.getTransactionType()==TransactionType.ISSUE)
            return issuance(initiateTransactionRequest,adminId);
        else
            return returnBook(initiateTransactionRequest,adminId);
    }

    private String issuance(InitiateTransactionRequest request,int adminId) throws Exception {
        /**
         * Steps in Issuing a transaction
         * 1. If the book is available or not and student is valid or not
         * 2. We need to check if student has reached maximum issuance limit
         * 3. Entry in the transaction table
         * 4. book to be assigned to a student -> update in book table
         */
        Student student = studentService.get(request.getStudentId());
        Admin admin = adminService.get(adminId);
        List<Book> books = bookService.find("id", String.valueOf(request.getBookId()));

        Book book = books != null && books.size()>0 ? books.get(0) : null;

        if(student == null
        || admin == null
        || book == null
        || book.getStudent()!=null
        || student.getBookList().size() >= maxBooksAllowed){
            throw new Exception("Invalid request");
        }

        Transaction transaction = null;

        try {
            transaction = Transaction.builder()
                    .txnId(UUID.randomUUID().toString())
                    .transactionType(TransactionType.ISSUE)
                    .transactionStatus(TransactionStatus.PENDING)
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .build();

            transactionRepository.save(transaction);
            book.setStudent(student);

            bookService.createOrUpdate(book);

            transaction.setTransactionStatus(TransactionStatus.COMPLETED);

        }
        catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }
        finally {
            transactionRepository.save(transaction);
        }
        return  transaction.getTxnId();
    }

    private String returnBook(InitiateTransactionRequest request,int adminId) throws Exception {
        /**
         * Steps in Returning a book
         * 1. If the book is valid or not and student is valid or not
         * 2. Entry in the transaction table
         * 3. due date check and fine calculation
         * 4. if there is no fine, the de-allocate the book from student's name ==>
         *  book table student will become null
         */
        Student student = studentService.get(request.getStudentId());
        Admin admin = adminService.get(adminId);
        List<Book> books = bookService.find("id", String.valueOf(request.getBookId()));

        Book book = books != null && books.size()>0 ? books.get(0) : null;

        if(student == null
                || admin == null
                || book == null
                || book.getStudent()==null
                || !book.getStudent().getId().equals(student.getId())){
            throw new Exception("Invalid request");
        }
        Transaction issuanceTransaction = transactionRepository.findTopByStudentAndBookAndTransactionTypeOrderByIdDesc(student,
        book,TransactionType.ISSUE);
        if(issuanceTransaction == null)
            throw new Exception("Invalid request");

        Transaction transaction = null;
        try {

            Integer fine = calculateFine(issuanceTransaction.getCreatedOn());

            transaction = Transaction.builder()
                    .txnId(UUID.randomUUID().toString())
                    .transactionType(TransactionType.ISSUE)
                    .transactionStatus(TransactionStatus.PENDING)
                    .student(student)
                    .book(book)
                    .admin(admin)
                    .fine(fine)
                    .build();

            if (fine == 0) {
                book.setStudent(null);
                bookService.createOrUpdate(book);
                transaction.setTransactionStatus(TransactionStatus.COMPLETED);
            }
        }
        catch (Exception e){
            transaction.setTransactionStatus(TransactionStatus.FAILURE);
        }
        finally {
            transactionRepository.save(transaction);
        }

        return transaction.getTxnId();


    }

    private Integer calculateFine(Date issuanceTime){
        long issuanceTimeInMilliSecs = issuanceTime.getTime();
        long currentTimeInMilliSecs = System.currentTimeMillis();

        long diff = currentTimeInMilliSecs - issuanceTimeInMilliSecs;
        long daysPassed = TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
        if(daysPassed > maxDuration){
            return (int)(daysPassed-maxDuration);
        }
        return 0;

    }

    public void makePayment(Integer studentId, String txnId, Integer fine) throws Exception {
        Transaction returnTxn = transactionRepository.findByTxnId(txnId);
        Book book = returnTxn.getBook();
        if(returnTxn.getFine()==fine && book.getStudent()!=null
        && book.getStudent().getId()==studentId){
            returnTxn.setTransactionStatus(TransactionStatus.COMPLETED);
            book.setStudent(null);
            bookService.createOrUpdate(book);
            transactionRepository.save(returnTxn);
        }
        else{
            throw new Exception("Invalid request");
        }
    }
}
