package com.hospital.hms.controller.api;

import com.hospital.hms.dto.PatientRecordDto;
import com.hospital.hms.service.PatientRecordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient-records")
public class PatientRecordApiController {
    private final PatientRecordService patientRecordService;

    public PatientRecordApiController(PatientRecordService patientRecordService) {
        this.patientRecordService = patientRecordService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientRecordDto> getRecord(@PathVariable Long patientId, @RequestParam(required = false) Long appointmentId, HttpSession session) {
        return ResponseEntity.ok(patientRecordService.getRecord(patientId, (Long) session.getAttribute("hms_user_id"), appointmentId));
    }
}
