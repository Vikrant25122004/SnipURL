package com.SnipURL.SnipURL.Services;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.User_Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class User_Service  {
    @Autowired
    private User_Repository user_repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServices emailServices;
    @Autowired
    private Redis_Service redisService;


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

    public boolean forgot(String username) {
        try {
            User user = redisService.get(username,User.class);
            if (user==null){
                User user1 = user_repository.findByusername(username);

                Random random = new Random();
                int number = 100000 + random.nextInt(900000); // ensures it's 6 digits
                String otp = String.valueOf(number);
                emailServices.messages(user1.getEmail(),"Forgot Password",otp);
                redisService.setLog("OTP_" + username, otp,300L);
                String oo= redisService.get("OTP_" + username,String.class);
                System.out.println(oo);
                return true;
            }
            else {

                Random random = new Random();
                int number = 100000 + random.nextInt(900000); // ensures it's 6 digits
                String otp = String.valueOf(number);
                emailServices.messages(user.getEmail(),"Forgot Password",otp);
                redisService.setLog("OTP_" + username, otp,300L);
                String oo= redisService.get("OTP_" + username,String.class);
                System.out.println(oo);
                return true;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean enterotp(String username,String otp){
        try {

            String storedotp = redisService.get("OTP_" + username , String.class);
            if(otp.trim().replaceAll("\\p{C}", "").equals(storedotp.trim().replaceAll("\\p{C}", ""))){


                return true;
            }
            else {
                return false;
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatepassword(String username,String password){
        User user = user_repository.findByusername(username);
        user.setPassword(passwordEncoder.encode(password));
        user_repository.save(user);
    }

    public void update(String username,User user) {
        User user1 = user_repository.findByusername(username);
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setName(user.getName());
        user_repository.save(user1);
    }
    public User Getuser(String username){
        System.out.println("Looking up user by username: " + username);
        User user = user_repository.findByusername(username);
        if (user== null){
            System.out.println("User not found for username: " + username);

            throw new RuntimeException("user not found with this username");
        }
        else {
            return user;
        }

    }
}
