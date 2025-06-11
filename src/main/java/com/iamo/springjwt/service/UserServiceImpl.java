package com.iamo.springjwt.service;

import com.iamo.springjwt.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements  UserService {
    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setUsername("IAmO");
        user.setEmail("pongchai@ggg.cmo");
        user.setRole("Admin");
        return user;
    }
}
