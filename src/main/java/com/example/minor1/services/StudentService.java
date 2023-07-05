package com.example.minor1.services;

import com.example.minor1.models.SecuredUser;
import com.example.minor1.models.Student;
import com.example.minor1.models.UserStatus;
import com.example.minor1.repositories.StudentCacheRepository;
import com.example.minor1.repositories.StudentRepository;
import com.example.minor1.utils.Constants;
import com.example.minor1.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserService userService;

    @Autowired
    StudentCacheRepository studentCacheRepository;

    public UserStatus create(Student student) {
        SecuredUser securedUser = student.getSecuredUser();
        securedUser.setAuthorities(Utils.getAuthoritiesForUser().get(Constants.STUDENT_USER));
        UserStatus securedUserStatus = userService.createIfNotPresent(securedUser);
        if(securedUserStatus!=UserStatus.SUCCESS){
            return securedUserStatus;
        }
        Student savedStudent = studentRepository.save(student);
        if(savedStudent!=null){
            return UserStatus.SUCCESS;
        }
        else{
            userService.delete(securedUser);
            return UserStatus.FAILURE;
        }
    }
    public Student get(Integer studentId) {
        // Here first we will check in cache, if not found then in database
        Student student = studentCacheRepository.get(studentId);
        if(student != null){
            return student;
        }

        student = studentRepository.findById(studentId).orElse(null);
        if(student != null){
            studentCacheRepository.set(student);
        }

        return student;
    }

    public boolean exist(Integer studentId) {
        return studentRepository.existsById(studentId);
    }
}
