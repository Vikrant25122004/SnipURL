package com.SnipURL.SnipURL.Services;

import com.SnipURL.SnipURL.Entity.My_order;
import com.SnipURL.SnipURL.Entity.URL;
import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.My_order_Repository;
import com.SnipURL.SnipURL.Repository.URL_Repository;
import com.SnipURL.SnipURL.Repository.User_Repository;
import com.SnipURL.SnipURL.Utils.Hmac_SHA256Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;


@Service
public class URL_Service {
    @Autowired
    private URL_Repository urlRepository;
    @Autowired
    private Hmac_SHA256Utils hmacSha256Utils;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private My_order_Repository myOrderRepository;
    public  String secretkey = "yT1u+P9vhRr8nGzv4CxC97aFLkPQyX5CeHjVY+2sBlQ";
    public static String link = "http://localhost:8080/";
    @Autowired
    private User_Repository user_repository;
    @Autowired
    public Redis_Service redisService;
    public String generateurl(String url,String username) throws JsonProcessingException {
        if (urlRepository.findByoriginal(url)==null){
            URL url1 = new URL();
            url1.setOriginal(url);
            url1.setCreated_At(LocalDateTime.now());
            url1.setExpire_By(LocalDateTime.now().plus(60,ChronoUnit.DAYS));
            String hash = hmacSha256Utils.generateHmacSHA256(url, secretkey);
            String shorturl = Base64.getUrlEncoder().withoutPadding().encodeToString(hash.getBytes()).substring(0, 6);
            url1.setShortURL(link + shorturl);
            urlRepository.save(url1);
            User user = redisService.get(username,User.class);
            if (user== null){
                User user1 = user_repository.findByusername(username);
                ArrayList<URL> urls = user1.getUrls();
                urls.add(url1);
                user1.setUrls(urls);
                user_repository.save(user1);
            }
            else {
                ArrayList<URL> urls = user.getUrls();
                urls.add(url1);
                user.setUrls(urls);
                user_repository.save(user);
            }
            return shorturl;
        }
        else {
            Random random = new Random();
            int dup = random.nextInt();
            String dupy = String.valueOf(dup);
            String hash = hmacSha256Utils.generateHmacSHA256(url + dupy , secretkey);
            String shorturl = Base64.getUrlEncoder().withoutPadding().encodeToString(hash.getBytes()).substring(0, 6);
            URL url2 = new URL();
            url2.setOriginal(url);
            url2.setCreated_At(LocalDateTime.now());
            url2.setExpire_By(LocalDateTime.now().plus(60,ChronoUnit.DAYS));
            url2.setShortURL(link + shorturl);
            url2.setOriginal(url);
            urlRepository.save(url2);
            User user = user_repository.findByusername(username);
            ArrayList<URL> urls = user.getUrls();
            urls.add(url2);
            user.setUrls(urls);
            user_repository.save(user);
            return url2.getShortURL();
        }
    }

    public void deleteurl(String url,String ShortURL, String username) {
        User user = user_repository.findByusername(username);
        Query query = new Query();
        query.addCriteria(Criteria.where(
                "shortURL").is(ShortURL));
        URL urls = mongoTemplate.findOne(query,URL.class);
        ArrayList<URL> urls1 = user.getUrls();
        urls1.remove(urls);
        user.setUrls(urls1);
        user_repository.save(user);
        urlRepository.delete(urls);






    }

    public String redirect(String shorturl) {
        Query query = new Query();
        query.addCriteria(Criteria.where("shortURL").is(shorturl));
        URL url = mongoTemplate.findOne(query,URL.class);
        if (url==null){
            return null;
        }
        if (url.getExpire_By().isBefore(LocalDateTime.now())){
            return null;
        }
        else {
            return url.getOriginal();
        }
    }

    public ArrayList<URL> geturls(String username) {
        User user = user_repository.findByusername(username);
        ArrayList<URL> urls = user.getUrls();
        if (urls.isEmpty()){
            return null;
        }
        else {
            return urls;
        }

    }
    public void recharge(URL url) throws RazorpayException {
        try {
            RazorpayClient client = new RazorpayClient("rzp_test_8LZboHNs3lEOMm", "4QpAyV4JCZ9XkjbazDUC1Wa0");
            JSONObject object = new JSONObject();
            object.put("amount", 50000);
            object.put("currency", "INR");
            object.put("receipt", LocalDateTime.now());
            Order order = client.orders.create(object);
            My_order myorder = new My_order();
            myorder.setAmount(order.get("amount"));
            myorder.setPaymentId(null);
            myorder.setStatus("created");
            myorder.setShorturl(url.getShortURL());
            myorder.setOrderid(order.get("id"));
            myorder.setReciept(order.get("receipt"));
            myOrderRepository.save(myorder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void extendexpiry(String username,String Shorturl){
        User user = user_repository.findByusername(username);
        ArrayList<URL> urls = user.getUrls();
        Query query = new Query();
        query.addCriteria(Criteria.where("shortURL").is(Shorturl));
        URL url = mongoTemplate.findOne(query,URL.class);
        urls.remove(url);
        url.setExpire_By(LocalDateTime.now().plus(60,ChronoUnit.DAYS));
        urlRepository.save(url);
        urls.add(url);
        user_repository.save(user);

    }
}
