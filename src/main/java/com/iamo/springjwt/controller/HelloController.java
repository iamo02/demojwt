package com.iamo.springjwt.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class HelloController {

    @GetMapping("/say")
    public String refundList() {
        return "Hello "+getCurrentUsername()  +" "+ getCurrentRole();
    }



    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }


        return null;
    }


    public String getCurrentRole() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().isEmpty()) {
            return auth.getAuthorities().iterator().next().getAuthority(); // เช่น "ROLE_ADMIN"
        }
        return null;
    }

}
