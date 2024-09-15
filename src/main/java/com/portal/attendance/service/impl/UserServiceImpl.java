package com.portal.attendance.service.impl;


import com.portal.attendance.Entity.User;
import com.portal.attendance.dao.UserRepository;
import com.portal.attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User saveUser(User user) {
        // Check if email is already present in the database
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        try {
            // Save the new user to the database
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save user due to a database error", e);
        }
    }

}
