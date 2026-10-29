package com.hospital.hms.controller.api;

import com.hospital.hms.dto.LoginRequest;
import com.hospital.hms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final UserService userService;

    @Autowired
    public AuthApiController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest,
                                                     HttpServletRequest request) {
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) existingSession.invalidate();
        var user = userService.authenticate(loginRequest);
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("hms_username", user.getUsername());
            session.setAttribute("hms_role", userService.effectiveRole(user));
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("username", user.getUsername());
            response.put("role", userService.effectiveRole(user));
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @org.springframework.web.bind.annotation.GetMapping("/me")
    public ResponseEntity<Map<String, Object>> currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        Object username = session.getAttribute("hms_username");
        Object role = session.getAttribute("hms_role");
        if (username == null || role == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(Map.of("username", username, "role", role));
    }

    @org.springframework.web.bind.annotation.PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }
}
