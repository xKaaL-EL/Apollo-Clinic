package com.hospital.hms.service.impl;

import com.hospital.hms.dto.DoctorDto;
import com.hospital.hms.dto.DoctorProfileUpdateDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Doctor;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.UserRepository;
import com.hospital.hms.model.entity.User;
import com.hospital.hms.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public DoctorDto createDoctor(DoctorDto doctorDto) {
        String username = doctorDto.getUsername() == null ? "" : doctorDto.getUsername().trim();
        if (username.isEmpty() || doctorDto.getPassword() == null || doctorDto.getPassword().isEmpty()
                || doctorDto.getConfirmPassword() == null || doctorDto.getConfirmPassword().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username, password and confirm password are required");
        }
        if (!doctorDto.getPassword().equals(doctorDto.getConfirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password and confirm password must match");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User account = new User();
        account.setUsername(username);
        account.setPassword(doctorDto.getPassword());
        account.setRole("DOCTOR");
        User savedAccount = userRepository.save(account);

        Doctor doctor = mapToEntity(doctorDto);
        doctor.setAccount(savedAccount);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToDto(savedDoctor);
    }

    @Override
    @Transactional
    public DoctorDto updateDoctor(Long id, DoctorDto doctorDto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

        String username = doctorDto.getUsername() == null ? "" : doctorDto.getUsername().trim();
        String newPassword = doctorDto.getPassword() == null ? "" : doctorDto.getPassword();
        String confirmPassword = doctorDto.getConfirmPassword() == null ? "" : doctorDto.getConfirmPassword();
        User account = doctor.getAccount();
        if (account == null) {
            if (!username.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor has no linked login account");
            }
        } else {
            if (username.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
            }
            userRepository.findByUsername(username).ifPresent(existing -> {
                if (!existing.getId().equals(account.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
                }
            });
            if (!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both new password fields are required");
                }
                if (!newPassword.equals(confirmPassword)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirmation must match");
                }
                account.setPassword(newPassword);
            }
            account.setUsername(username);
            userRepository.save(account);
        }

        doctor.setName(doctorDto.getName());
        doctor.setGender(doctorDto.getGender());
        doctor.setAge(doctorDto.getAge());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setContact(doctorDto.getContact());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return mapToDto(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        doctorRepository.delete(doctor);
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
        return mapToDto(doctor);
    }

    @Override
    public List<DoctorDto> getAllDoctors(String sortBy) {
        List<Doctor> doctors;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "NAME":
                doctors = doctorRepository.findAllByOrderByNameAsc();
                break;
            case "AGE":
                doctors = doctorRepository.findAllByOrderByAgeAsc();
                break;
            default:
                doctors = doctorRepository.findAllByOrderByIdAsc();
                break;
        }
        return doctors.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDto> searchDoctors(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllDoctors("ID");
        }
        return doctorRepository.findByNameContainingIgnoreCaseOrSpecializationContainingIgnoreCase(query, query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDto getProfileByAccountId(Long accountId) {
        Doctor doctor = doctorRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
        return mapToDto(doctor);
    }

    @Override
    @Transactional
    public DoctorDto updateProfileByAccountId(Long accountId, DoctorProfileUpdateDto profile) {
        Doctor doctor = doctorRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
        User account = doctor.getAccount();
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor has no linked login account");
        }

        String username = profile.getUsername() == null ? "" : profile.getUsername().trim();
        if (username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        userRepository.findByUsername(username).ifPresent(existing -> {
            if (!existing.getId().equals(account.getId())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
            }
        });

        String newPassword = profile.getPassword() == null ? "" : profile.getPassword();
        String confirmPassword = profile.getConfirmPassword() == null ? "" : profile.getConfirmPassword();
        if (!newPassword.isBlank() || !confirmPassword.isBlank()) {
            if (newPassword.isBlank() || confirmPassword.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both new password fields are required");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirmation must match");
            }
            account.setPassword(newPassword);
        }

        account.setUsername(username);
        doctor.setSpecialization(profile.getSpecialization().trim());
        doctor.setContact(profile.getContact().trim());
        userRepository.save(account);
        return mapToDto(doctorRepository.save(doctor));
    }

    private DoctorDto mapToDto(Doctor doctor) {
        DoctorDto dto = new DoctorDto(
                doctor.getId(),
                doctor.getName(),
                doctor.getGender(),
                doctor.getAge(),
                doctor.getSpecialization(),
                doctor.getContact()
        );
        if (doctor.getAccount() != null) {
            dto.setUsername(doctor.getAccount().getUsername());
        }
        return dto;
    }

    private Doctor mapToEntity(DoctorDto doctorDto) {
        return new Doctor(
                null,
                doctorDto.getName(),
                doctorDto.getGender(),
                doctorDto.getAge(),
                doctorDto.getSpecialization(),
                doctorDto.getContact()
        );
    }
}
