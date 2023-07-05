package com.example.minor1.dtos;

import com.example.minor1.models.TransactionType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiateTransactionRequest {

    @NotNull
    private Integer studentId;

    /**
     *     Should not be taken from frontend, but should be taken from security context

     *     Whenever an action triggered by authenticated user, always take user_id from the
     *     authentication context amd never take it from request. Only this way, we can ensure
     *     proper security in the application. Like in the below example.
     *     Ex: student can only see his details, admin can see every student details using student_id
     *
     *     Here, since admin is required in initiating every transaction, if we take admin from frontend,
     *     any admin can name other admin name in transaction thereby misleading us when we want to know
     *     which admin did transaction later
     *
     *     --> So we remove admin_id from frontend request
      */
    /*
    @NotNull
    private Integer adminId;
     */

    @NotNull
    private Integer bookId;

    @NotNull
    private TransactionType transactionType;

}
