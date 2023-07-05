package com.example.minor1.controllers;

import com.example.minor1.dtos.CreateStudentRequest;
import com.example.minor1.models.SecuredUser;
import com.example.minor1.models.Student;
import com.example.minor1.models.UserStatus;
import com.example.minor1.services.StudentService;
import com.example.minor1.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
public class StudentController {

    private final static Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentService studentService;

    // make this api as signUp api for student, so can be unsecured
    @PostMapping("/student/signup")
    public UserStatus createStudent(@RequestBody @Valid CreateStudentRequest createStudentRequest){
        return studentService.create(createStudentRequest.toStudent());
    }

    // this must be secured, admin can see everyone's detail but student should
    // only see their own details, so only admin can access this
    @GetMapping("/student/{id}")
    public Student getStudentById(@PathVariable("id") Integer studentId) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();

        for(GrantedAuthority grantedAuthority: securedUser.getAuthorities()){
            String[] authorities = grantedAuthority.getAuthority().split(Constants.DELIMITER);
            boolean isCalledByAdmin = Arrays.stream(authorities).anyMatch(x -> Constants.STUDENT_INFO_AUTHORITY.equals(x));
            if (isCalledByAdmin) {
                return studentService.get(studentId);
            }

        }
        throw new Exception("User is not authorized to do this");

    }


    // using this api, student can fetch his own details, not passing parameter, through security
    // context, who is requesting should be known and their details should be returned
    @GetMapping("/student")
    public Student getStudent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser user = (SecuredUser) authentication.getPrincipal();
        int studentId = user.getStudent().getId();

        return studentService.get(studentId);
    }
}
