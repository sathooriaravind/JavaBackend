package com.example.minor1.controllers;

import com.example.minor1.dtos.InitiateTransactionRequest;
import com.example.minor1.models.SecuredUser;
import com.example.minor1.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public String initiateTxn(@RequestBody @Valid InitiateTransactionRequest initiateTransactionRequest) throws Exception {
        // txnId is returned as string

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser user = (SecuredUser) authentication.getPrincipal();
        int adminId = user.getAdmin().getId();
        return transactionService.initiateTxn(initiateTransactionRequest,adminId);
    }

    /**
     *    Whenever an action triggered by authenticated user, always take user_id from the
     *    authentication context amd never take it from request. Only this way, we can ensure
     *    proper security in the application. Like in the below example.
     *
     *    Here, student_id should be obtained from security context rather than as parameter
     *    because particular student makes payment for oneself.
     *
     *    -->   so we won't take studentId as parameter from frontend
     */
    @PostMapping("/transaction/payment")
    public void makePayment(@RequestParam("transactionId") String txnId,
                            @RequestParam("fineAmount") Integer fine) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser user = (SecuredUser) authentication.getPrincipal();
        int studentId = user.getStudent().getId();

        transactionService.makePayment(studentId,txnId,fine);
    }

}
