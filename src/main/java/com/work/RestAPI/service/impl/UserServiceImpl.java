package com.work.RestAPI.service.impl;

import com.work.RestAPI.model.User;
import com.work.RestAPI.repository.UserRepository;
import com.work.RestAPI.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public User updateUser(Long id, User user) {
        User existing = repository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(user.getName());
            existing.setEmail(user.getEmail());
            return repository.save(existing);
        }
        return null;
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}
