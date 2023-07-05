package com.example.minor1.services;

import com.example.minor1.models.SecuredUser;
import com.example.minor1.models.UserStatus;
import com.example.minor1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByusername(username);
    }

    public UserStatus createIfNotPresent(SecuredUser securedUser) {
        List<SecuredUser> securedUsers = userRepository.findAll();
        for(SecuredUser user:securedUsers){
            if(user.getUsername().equals(securedUser.getUsername())){
                return UserStatus.USER_ALREADY_EXISTS;
            }
        }
        securedUser.setPassword(passwordEncoder.encode(securedUser.getPassword()));
        SecuredUser userUpdated = userRepository.save(securedUser);
        // When save doesn't succeed it will throw exception, see the warning that tells
        // having below if condition is redundant and think what to do
        if(userUpdated!=null){
            return UserStatus.SUCCESS;
        }
        return UserStatus.FAILURE;
    }

    public void delete(SecuredUser securedUser){
        userRepository.delete(securedUser);
    }


}
