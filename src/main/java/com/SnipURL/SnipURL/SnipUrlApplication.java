package com.SnipURL.SnipURL;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.User_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SnipUrlApplication {
	public static void main(String[] args) {
		SpringApplication.run(SnipUrlApplication.class, args);
	}

}
