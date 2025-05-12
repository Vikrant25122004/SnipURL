package com.SnipURL.SnipURL.Filter;

import com.SnipURL.SnipURL.Services.User_Detail_Service;
import com.SnipURL.SnipURL.Utils.JWT_UTILS;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWT_Filter extends OncePerRequestFilter {
    @Autowired
    private JWT_UTILS jwtUtils;
    @Autowired
    private User_Detail_Service userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String username = null;
        String jwt = null;
        // ✅ Extract JWT from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                }
            }
        }
        if (jwt !=null){
            username = jwtUtils.extractUsername(jwt);
        }
        if (username!=null){
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                if (jwtUtils.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }


        }
        filterChain.doFilter(request,response);
    }
}
