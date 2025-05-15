package com.SnipURL.SnipURL.Controller;

import com.SnipURL.SnipURL.Entity.My_order;
import com.SnipURL.SnipURL.Entity.URL;
import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.My_order_Repository;
import com.SnipURL.SnipURL.Services.URL_Service;
import com.SnipURL.SnipURL.Services.User_Service;
import com.razorpay.RazorpayClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/User")
public class User_Controller {
    @Autowired
    private User_Service userService;
    @Autowired
    private URL_Service urlService;
    @Autowired
    private My_order_Repository myOrderRepository;
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
    @GetMapping("/URLS")
    public ResponseEntity<?> urlss(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ArrayList<URL> urls = urlService.geturls(username);
            if (urls==null){
                return new ResponseEntity<>("you have not generated any url yet", HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity<>(urls,HttpStatus.OK);
            }


        }
        catch (Exception e){
            throw new RuntimeException();
        }
    }
    @PostMapping("/Recharge")
    public ResponseEntity<?> recharge(@RequestBody URL url ){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            urlService.recharge(url);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();;
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody Map<String, Object> data){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        My_order myorder1 = this.myOrderRepository.findBymyOrderid((Long) data.get("order_id"));
        myorder1.setPaymentId(data.get("payment_id").toString());
        myorder1.setStatus(data.get("status").toString());
        myOrderRepository.save(myorder1);
        try {
            if (myorder1.getStatus()=="Paid" || myorder1.getStatus()=="paid"){
                String shorturl = myorder1.getShorturl();
                urlService.extendexpiry(username,shorturl);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                throw new RuntimeException("payment not Successful");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
