package com.example.springbootsecurity.service.impl;

import com.example.springbootsecurity.model.Role;
import com.example.springbootsecurity.model.User;
import com.example.springbootsecurity.repository.RoleRepository;
import com.example.springbootsecurity.repository.UserRepository;
import com.example.springbootsecurity.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>());
        return user;
    }

    @Override
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    @Override
    public void addToUser(String username, String rolename){
        if(!userRepository.findByEmail(username).isPresent()){
            throw new IllegalArgumentException(("User with email "+username+" does not exist."));
        }
        Role role = roleRepository.findByName(rolename);
        if(role == null){
           throw new IllegalArgumentException("Role with name "+rolename+" does not exist.");
        }
        User user = userRepository.findByEmail(username).get();
        user.getRoles().add(role);
    }


}
