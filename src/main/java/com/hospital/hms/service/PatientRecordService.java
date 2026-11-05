package com.hospital.hms.service;

import com.hospital.hms.dto.PatientRecordDto;

public interface PatientRecordService {
    PatientRecordDto getRecord(Long patientId, Long userId);
    PatientRecordDto getRecord(Long patientId, Long userId, Long appointmentId);
}
