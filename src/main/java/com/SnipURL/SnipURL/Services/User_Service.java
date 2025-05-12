package com.SnipURL.SnipURL.Services;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.User_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class User_Service  {
    @Autowired
    private User_Repository user_repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServices emailServices;

    public void signupuser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user_repository.save(user);
            emailServices.messages(user.getEmail(),"User Registration","You have successfully registered on SnipURL");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("something went wrong");
        }

    }
}
