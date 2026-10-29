package com.hospital.hms.controller.api;

import com.hospital.hms.dto.BillDto;
import com.hospital.hms.service.PdfDocumentService;
import com.hospital.hms.service.BillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillApiController {

    private final BillService billService;
    private final PdfDocumentService pdfDocumentService;

    @Autowired
    public BillApiController(BillService billService, PdfDocumentService pdfDocumentService) {
        this.billService = billService;
        this.pdfDocumentService = pdfDocumentService;
    }

    @GetMapping
    public ResponseEntity<List<BillDto>> getAllBills(@RequestParam(value = "sortBy", required = false) String sortBy) {
        return ResponseEntity.ok(billService.getAllBills(sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BillDto>> searchBills(@RequestParam("query") String query) {
        return ResponseEntity.ok(billService.searchBills(query));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(billService.getBillById(id));
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> viewBillPdf(@PathVariable("id") Long id) {
        return pdfResponse(pdfDocumentService.generateBillPdf(id), id, false);
    }

    @GetMapping("/{id}/pdf/download")
    public ResponseEntity<byte[]> downloadBillPdf(@PathVariable("id") Long id) {
        return pdfResponse(pdfDocumentService.generateBillPdf(id), id, true);
    }

    private ResponseEntity<byte[]> pdfResponse(byte[] pdf, Long id, boolean download) {
        String disposition = (download ? "attachment" : "inline") + "; filename=\"invoice-" + id + ".pdf\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping
    public ResponseEntity<BillDto> createBill(@Valid @RequestBody BillDto billDto) {
        return new ResponseEntity<>(billService.createBill(billDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillDto> updateBill(@PathVariable("id") Long id, @Valid @RequestBody BillDto billDto) {
        return ResponseEntity.ok(billService.updateBill(id, billDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable("id") Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
