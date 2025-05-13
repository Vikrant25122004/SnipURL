package com.SnipURL.SnipURL.Controller;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Services.User_Detail_Service;
import com.SnipURL.SnipURL.Services.User_Service;
import com.SnipURL.SnipURL.Utils.JWT_UTILS;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class Public {
    @Autowired
    private User_Service userService;
    @Autowired
    private User_Detail_Service userDetailService;
    @Autowired
    private JWT_UTILS jwtUtils;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user){
        try {
            userService.signupuser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> Loginrep(@RequestBody  User user, HttpServletResponse response){
        try{
            UserDetails userDetails = userDetailService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtils.generateToken(userDetails.getUsername());
            Cookie cookie = new Cookie("jwt",jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(cookie);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/forgot/{username}")
    public ResponseEntity forgot(@PathVariable String username){
        try {
            userService.forgot(username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/enterotp/{username}")
    public ResponseEntity enterotp(@PathVariable String username, @RequestParam String otp, @RequestParam String password){
        try {
            if (
            userService.enterotp(username,otp)==true){
                userService.updatepassword(username, password);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("incorrect otp",HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
