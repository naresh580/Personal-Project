package com.work.RestAPI.service;

import com.work.RestAPI.model.User;

import java.util.List;

public interface UserService {
    void deleteUser(Long id);
    User updateUser(Long id, User user);
    User createUser(User user);
    public User getUserById(Long id);
    List<User> getAllUsers();
    }
