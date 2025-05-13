package com.SnipURL.SnipURL.Services;

import com.SnipURL.SnipURL.Repository.URL_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class URL_Service {
    @Autowired
    private URL_Repository urlRepository;
    public String generateurl(String url){
        if (urlRepository.findByoriginal(url)==null){

        }
    }

}
