package com.iamo.springjwt.service;

import com.iamo.springjwt.model.User;

public interface UserService {

     User findByUsername(String username);

}
