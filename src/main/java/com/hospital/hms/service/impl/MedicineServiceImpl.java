package com.hospital.hms.service.impl;

import com.hospital.hms.dto.MedicineDto;
import com.hospital.hms.exception.ResourceNotFoundException;
import com.hospital.hms.model.entity.Medicine;
import com.hospital.hms.repository.MedicineRepository;
import com.hospital.hms.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public MedicineDto createMedicine(MedicineDto dto) {
        Medicine medicine = mapToEntity(dto);
        Medicine saved = medicineRepository.save(medicine);
        return mapToDto(saved);
    }

    @Override
    public MedicineDto updateMedicine(Long id, MedicineDto dto) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicine.setName(dto.getName());
        medicine.setStock(dto.getStock());
        medicine.setPrice(dto.getPrice());

        Medicine updated = medicineRepository.save(medicine);
        return mapToDto(updated);
    }

    @Override
    public void deleteMedicine(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        medicineRepository.delete(medicine);
    }

    @Override
    public MedicineDto getMedicineById(Long id) {
        Medicine medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return mapToDto(medicine);
    }

    @Override
    public List<MedicineDto> getAllMedicines(String sortBy) {
        List<Medicine> medicines;
        if (sortBy == null) {
            sortBy = "ID";
        }
        switch (sortBy.toUpperCase()) {
            case "NAME":
                medicines = medicineRepository.findAllByOrderByNameAsc();
                break;
            case "STOCK":
                medicines = medicineRepository.findAllByOrderByStockAsc();
                break;
            case "PRICE":
                medicines = medicineRepository.findAllByOrderByPriceAsc();
                break;
            default:
                medicines = medicineRepository.findAllByOrderByIdAsc();
                break;
        }
        return medicines.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<MedicineDto> searchMedicines(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllMedicines("ID");
        }
        return medicineRepository.findByNameContainingIgnoreCase(query)
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private MedicineDto mapToDto(Medicine medicine) {
        return new MedicineDto(
                medicine.getId(),
                medicine.getName(),
                medicine.getStock(),
                medicine.getPrice()
        );
    }

    private Medicine mapToEntity(MedicineDto dto) {
        return new Medicine(
                dto.getId(),
                dto.getName(),
                dto.getStock(),
                dto.getPrice()
        );
    }
}
