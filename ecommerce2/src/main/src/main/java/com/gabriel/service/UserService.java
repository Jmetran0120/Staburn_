package com.gabriel.service;

import com.gabriel.model.User;

public interface UserService {
    User findByEmail(String email);
    User create(User user, String password);
    User findById(Integer id);
}

