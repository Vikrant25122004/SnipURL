package com.SnipURL.SnipURL.Controller;

import com.SnipURL.SnipURL.Entity.URL;
import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Services.URL_Service;
import com.SnipURL.SnipURL.Services.User_Service;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/User")
public class User_Controller {
    @Autowired
    private User_Service userService;
    @Autowired
    private URL_Service urlService;
    @PutMapping("/update/{username}")
    public ResponseEntity<?> update(@PathVariable String username, @RequestBody User user){
        try {
            userService.update(username,user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createURL(@RequestParam  String url){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            String urll = urlService.generateurl(url,username);
            return new ResponseEntity<>(urll,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteurl(@RequestParam String original, @RequestParam String shortURL){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            urlService.deleteurl(original, shortURL,username);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
