package com.example.minor1.controllers;

import com.example.minor1.dtos.CreateAdminRequest;
import com.example.minor1.models.UserStatus;
import com.example.minor1.services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @PostMapping("/admin")
    public UserStatus createAdmin(@RequestBody @Valid CreateAdminRequest createAdminRequest){
        return adminService.create(createAdminRequest.toAdmin());
    }

}
