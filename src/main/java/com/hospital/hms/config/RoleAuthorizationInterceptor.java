package com.hospital.hms.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Set;

public class RoleAuthorizationInterceptor implements HandlerInterceptor {
    private static final Set<String> RECEPTIONIST_CRUD = Set.of("patients", "appointments");
    private static final Set<String> RECEPTIONIST_READ_ONLY = Set.of("doctors", "medicines", "followups");
    private static final Set<String> RECEPTIONIST_PDF = Set.of("bills", "prescriptions");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String path = request.getRequestURI();
        if (path.equals("/login") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/")) return true;
        if (path.startsWith("/api/auth/")) return true;

        HttpSession session = request.getSession(false);
        String role = session == null ? null : (String) session.getAttribute("hms_role");
        if (role == null || (!role.equals("ADMIN") && !role.equals("RECEPTIONIST") && !role.equals("DOCTOR"))) {
            if (path.startsWith("/api/")) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication required");
            } else {
                response.sendRedirect("/login");
            }
            return false;
        }
        if (role.equals("DOCTOR")) {
            if (!path.startsWith("/api/") || path.equals("/api/doctors/me") || path.startsWith("/api/appointments/doctor/")
                    || path.startsWith("/api/patient-records/") || path.startsWith("/api/prescriptions")
                    || path.startsWith("/api/followups")) {
                if (path.startsWith("/api/") && !request.getMethod().equals("GET")) {
                    String[] parts = path.split("/");
                    if (parts.length < 3 || (!parts[2].equals("prescriptions") && !parts[2].equals("followups")
                            && !path.equals("/api/doctors/me"))) return deny(response);
                }
                return true;
            }
            return deny(response);
        }
        if (role.equals("ADMIN") || !path.startsWith("/api/")) return true;

        String[] parts = path.split("/");
        if (parts.length < 3) return deny(response);
        String resource = parts[2];
        String method = request.getMethod();
        boolean allowed = RECEPTIONIST_CRUD.contains(resource)
                || (RECEPTIONIST_READ_ONLY.contains(resource) && method.equals("GET"))
                || (RECEPTIONIST_PDF.contains(resource) && method.equals("GET"));
        return allowed || deny(response);
    }

    private boolean deny(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value(), "Insufficient permissions");
        return false;
    }
}
