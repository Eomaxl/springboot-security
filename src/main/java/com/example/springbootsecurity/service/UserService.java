package com.example.springbootsecurity.service;

import com.example.springbootsecurity.model.Role;
import com.example.springbootsecurity.model.User;

public interface UserService {

    User saveUser(User user);
    Role saveRole(Role role);
    void addToUser(String username, String rolename);
}
