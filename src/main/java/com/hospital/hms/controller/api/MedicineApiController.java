package com.hospital.hms.controller.api;

import com.hospital.hms.dto.MedicineDto;
import com.hospital.hms.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class MedicineApiController {

    private final MedicineService medicineService;

    @Autowired
    public MedicineApiController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping
    public ResponseEntity<List<MedicineDto>> getAllMedicines(@RequestParam(value = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(medicineService.getAllMedicines(sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineDto>> searchMedicines(@RequestParam("query") String query) {
        return ResponseEntity.ok(medicineService.searchMedicines(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineDto> getMedicineById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(medicineService.getMedicineById(id));
    }

    @PostMapping
    public ResponseEntity<MedicineDto> createMedicine(@Valid @RequestBody MedicineDto medicineDto) {
        return new ResponseEntity<>(medicineService.createMedicine(medicineDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineDto> updateMedicine(@PathVariable("id") Long id, @Valid @RequestBody MedicineDto medicineDto) {
        return ResponseEntity.ok(medicineService.updateMedicine(id, medicineDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable("id") Long id) {
        medicineService.deleteMedicine(id);
        return ResponseEntity.noContent().build();
    }
}
