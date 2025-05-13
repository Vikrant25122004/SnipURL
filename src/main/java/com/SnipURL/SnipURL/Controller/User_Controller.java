package com.SnipURL.SnipURL.Controller;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Services.URL_Service;
import com.SnipURL.SnipURL.Services.User_Service;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class User_Controller {
    @Autowired
    private User_Service userService;
    @Autowired
    private URL_Service urlService;
    @PutMapping("/update/{username}")
    private ResponseEntity<?> update(@PathVariable String username, @RequestBody User user){
        try {
            userService.update(username,user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/create")
    private ResponseEntity<?> createURL(String url){
        try{
            String urll = urlService.generateurl(url);
            return new ResponseEntity<>(urll,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
