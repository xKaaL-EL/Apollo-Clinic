package com.hospital.hms.service.impl;

import com.hospital.hms.dto.BillDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Bill;
import com.hospital.hms.model.entity.Patient;
import com.hospital.hms.repository.BillRepository;
import com.hospital.hms.repository.PatientRepository;
import com.hospital.hms.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository, PatientRepository patientRepository) {
        this.billRepository = billRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public BillDto createBill(BillDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        Bill bill = new Bill();
        bill.setPatient(patient);
        bill.setAmount(dto.getAmount());
        bill.setStatus(dto.getStatus());

        Bill saved = billRepository.save(bill);
        return mapToDto(saved);
    }

    @Override
    public BillDto updateBill(Long id, BillDto dto) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        bill.setPatient(patient);
        bill.setAmount(dto.getAmount());
        bill.setStatus(dto.getStatus());

        Bill updated = billRepository.save(bill);
        return mapToDto(updated);
    }

    @Override
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        billRepository.delete(bill);
    }

    @Override
    public BillDto getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
        return mapToDto(bill);
    }

    @Override
    public List<BillDto> getAllBills(String sortBy) {
        List<Bill> bills;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "AMOUNT":
                bills = billRepository.findAllByOrderByAmountAsc();
                break;
            case "STATUS":
                bills = billRepository.findAllByOrderByStatusAsc();
                break;
            default:
                bills = billRepository.findAllByOrderByIdAsc();
                break;
        }
        return bills.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<BillDto> searchBills(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllBills("ID");
        }
        return billRepository.findByPatientNameContainingIgnoreCase(query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private BillDto mapToDto(Bill bill) {
        return new BillDto(
                bill.getId(),
                bill.getPatient().getId(),
                bill.getPatient().getName(),
                bill.getAmount(),
                bill.getStatus()
        );
    }
}
