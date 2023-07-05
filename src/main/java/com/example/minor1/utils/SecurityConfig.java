package com.example.minor1.utils;

import com.example.minor1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf()
                .ignoringAntMatchers("/student/signup")
                .and()

                .httpBasic().and()
                .csrf().disable()// this will consider all request as http requests and return http code instead of redirection to login page
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,"/Student").hasAuthority(Constants.STUDENT_SELF_INFO_AUTHORITY)
                .antMatchers(HttpMethod.GET,"/student/**").hasAuthority(Constants.STUDENT_INFO_AUTHORITY)
                .antMatchers(HttpMethod.POST,"/admin**").hasAuthority(Constants.CREATE_ADMIN_AUTHORITY)
                .antMatchers(HttpMethod.GET, "/book/**").hasAuthority(Constants.READ_BOOK_AUTHORITY)
                .antMatchers(HttpMethod.POST, "/book/**").hasAuthority(Constants.CREATE_BOOK_AUTHORITY)
                .antMatchers("/transaction/payment/**").hasAuthority(Constants.MAKE_PAYMENT_AUTHORITY)
                .antMatchers("/transaction/**").hasAuthority(Constants.INITIATE_TRANSACTION_AUTHORITY)
                .antMatchers("/**").permitAll()
                .and()
                .formLogin();
    }


}
