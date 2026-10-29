package com.hospital.hms.service;

import com.hospital.hms.dto.AppointmentDto;
import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto appointmentDto);
    AppointmentDto updateAppointment(Long id, AppointmentDto appointmentDto);
    void deleteAppointment(Long id);
    AppointmentDto getAppointmentById(Long id);
    List<AppointmentDto> getAllAppointments(String sortBy);
    List<AppointmentDto> searchAppointments(String query);
}
