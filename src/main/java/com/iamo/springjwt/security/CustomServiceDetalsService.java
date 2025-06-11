package com.iamo.springjwt.security;

import com.iamo.springjwt.model.User;
import com.iamo.springjwt.service.UserServiceImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomServiceDetalsService implements UserDetailsService {

    private final UserServiceImpl userService;

    public CustomServiceDetalsService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);

        if(user!=null){
            Set<GrantedAuthority> role = new HashSet<>();
            role.add(new SimpleGrantedAuthority(user.getRole()));

            List<GrantedAuthority> authorityList = new ArrayList<GrantedAuthority>(role);

            return new org.springframework.security.core.userdetails.User(user.getUsername(),"password",authorityList);
        }

       throw  new UsernameNotFoundException("User ผิด");
    }
}
