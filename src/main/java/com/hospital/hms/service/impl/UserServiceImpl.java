package com.hospital.hms.service.impl;

import com.hospital.hms.dto.LoginRequest;
import com.hospital.hms.model.entity.User;
import com.hospital.hms.repository.UserRepository;
import com.hospital.hms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(loginRequest.getPassword()) && effectiveRole(user) != null) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String effectiveRole(User user) {
        if (user == null || user.getRole() == null) return null;
        return switch (user.getRole().trim().toUpperCase()) {
            case "ADMIN" -> "ADMIN";
            case "RECEPTIONIST", "STAFF" -> "RECEPTIONIST";
            default -> null;
        };
    }

    @Override
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Simple storage matches legacy requirement
        user.setRole("ADMIN");
        return userRepository.save(user);
    }
}
