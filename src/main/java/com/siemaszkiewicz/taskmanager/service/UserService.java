package com.siemaszkiewicz.taskmanager.service;

import com.siemaszkiewicz.taskmanager.model.User;

public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
