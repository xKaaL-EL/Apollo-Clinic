package com.hospital.hms.service;

import com.hospital.hms.dto.LoginRequest;
import com.hospital.hms.model.entity.User;

public interface UserService {
    User authenticate(LoginRequest loginRequest);
    String effectiveRole(User user);
    User register(String username, String password);
}
