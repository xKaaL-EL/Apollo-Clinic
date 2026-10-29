package com.hospital.hms.controller.api;

import com.hospital.hms.dto.PrescriptionDto;
import com.hospital.hms.service.PrescriptionService;
import com.hospital.hms.service.PdfDocumentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionApiController {

    private final PrescriptionService prescriptionService;
    private final PdfDocumentService pdfDocumentService;

    @Autowired
    public PrescriptionApiController(PrescriptionService prescriptionService, PdfDocumentService pdfDocumentService) {
        this.prescriptionService = prescriptionService;
        this.pdfDocumentService = pdfDocumentService;
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions(@RequestParam(value = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions(sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PrescriptionDto>> searchPrescriptions(@RequestParam("query") String query) {
        return ResponseEntity.ok(prescriptionService.searchPrescriptions(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> viewPrescriptionPdf(@PathVariable("id") Long id) {
        return pdfResponse(pdfDocumentService.generatePrescriptionPdf(id), id, false);
    }

    @GetMapping("/{id}/pdf/download")
    public ResponseEntity<byte[]> downloadPrescriptionPdf(@PathVariable("id") Long id) {
        return pdfResponse(pdfDocumentService.generatePrescriptionPdf(id), id, true);
    }

    private ResponseEntity<byte[]> pdfResponse(byte[] pdf, Long id, boolean download) {
        String disposition = (download ? "attachment" : "inline") + "; filename=\"prescription-" + id + ".pdf\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping
    public ResponseEntity<PrescriptionDto> createPrescription(@Valid @RequestBody PrescriptionDto prescriptionDto) {
        return new ResponseEntity<>(prescriptionService.createPrescription(prescriptionDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDto> updatePrescription(@PathVariable("id") Long id, @Valid @RequestBody PrescriptionDto prescriptionDto) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, prescriptionDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable("id") Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
