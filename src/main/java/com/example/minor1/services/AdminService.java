package com.example.minor1.services;

import com.example.minor1.models.Admin;
import com.example.minor1.models.SecuredUser;
import com.example.minor1.models.UserStatus;
import com.example.minor1.repositories.AdminRepository;
import com.example.minor1.utils.Constants;
import com.example.minor1.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserService userService;

    public UserStatus create(Admin admin) {
        SecuredUser securedUser = admin.getSecuredUser();
        securedUser.setAuthorities(Utils.getAuthoritiesForUser().get(Constants.ADMIN_USER));
        UserStatus securedUserStatus = userService.createIfNotPresent(securedUser);
        if(securedUserStatus!=UserStatus.SUCCESS){
            return securedUserStatus;
        }
        Admin savedAdmin = adminRepository.save(admin);
        if(savedAdmin!=null){
            return UserStatus.SUCCESS;
        }
        else{
            userService.delete(securedUser);
            return UserStatus.FAILURE;
        }
    }

    public Admin get(Integer adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }
}
