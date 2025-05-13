package com.SnipURL.SnipURL.Services;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.User_Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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
                SecureRandom random = new SecureRandom();
                StringBuilder otp = new StringBuilder();

                for (int i = 0; i < 6; i++) {
                    int digit = random.nextInt(10);  // generates a digit from 0 to 9
                    otp.append(digit);
                }
                emailServices.messages(user1.getEmail(),"Forgot Password",otp.toString());
                redisService.setLog("OTP_" + username, otp,300L);
                return true;
            }
            else {

                SecureRandom random = new SecureRandom();
                StringBuilder otp = new StringBuilder();

                for (int i = 0; i < 9; i++) {
                    int digit = random.nextInt(10);  // generates a digit from 0 to 9
                    otp.append(digit);
                }
                emailServices.messages(user.getEmail(),"Forgot Password",otp.toString());
                redisService.setLog("OTP_" + username, otp.toString(),300L);
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
            if (otp == storedotp){

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
}
