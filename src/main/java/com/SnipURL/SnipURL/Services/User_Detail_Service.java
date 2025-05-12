package com.SnipURL.SnipURL.Services;

import com.SnipURL.SnipURL.Entity.User;
import com.SnipURL.SnipURL.Repository.User_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class User_Detail_Service implements UserDetailsService {
    @Autowired
    public User_Repository user_repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = user_repository.findByusername(username);
        if (user!=null){
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(username)
                    .password(user.getPassword())
                    .build();
            return userDetails;
        }
        throw new UsernameNotFoundException("user not found with this username");
    }
}
