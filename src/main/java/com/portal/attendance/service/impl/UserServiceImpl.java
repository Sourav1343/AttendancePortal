package com.portal.attendance.service.impl;


import com.portal.attendance.Entity.User;
import com.portal.attendance.dao.UserRepository;
import com.portal.attendance.exception.DuplicateEmailException;
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
        userRepository.findByEmail(user.getEmail())
                     .ifPresent(existingUser -> {
                      throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists");
                  });

        return userRepository.save(user);
    }

}
