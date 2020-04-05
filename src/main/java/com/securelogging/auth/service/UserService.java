package com.securelogging.auth.service;

import com.securelogging.auth.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findByUsername(String username);

    List<User> findAll();

    User findByEmail(String email);
}
