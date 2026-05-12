package com.substring.auth.auth_app_backend.services;

import com.substring.auth.auth_app_backend.dtos.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto getUserByEmail(String email);

    UserDto updateUser(UserDto user, String userId);

    UserDto getUserById(String userId);

    void deleteUser(String userId);

    Iterable<UserDto> getAllUsers();
}
