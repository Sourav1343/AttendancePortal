package com.portal.attendance.controller;


import com.portal.attendance.Entity.User;
import com.portal.attendance.exception.DuplicateEmailException;
import com.portal.attendance.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private  UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        logger.info("Received request to create user: {}", user.getUserId());

        // Check for validation errors in request body
        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors found for user: {}", user.getUserId());
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                logger.warn("Validation error on field '{}': {}", error.getField(), error.getDefaultMessage());
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            // Delegate the user creation to the service
            userService.saveUser(user);
            logger.info("User created successfully: {}", user.getUserId());
            return ResponseEntity.ok("User created successfully");
        } catch (DuplicateEmailException e) {
            logger.error("Error creating user {}: {}", user.getUserId(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Internal error occurred while creating user {}: {}", user.getUserId(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An internal error occurred");
        }
    }

}
