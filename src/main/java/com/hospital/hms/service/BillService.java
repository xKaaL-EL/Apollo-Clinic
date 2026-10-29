package com.hospital.hms.service;

import com.hospital.hms.dto.BillDto;
import java.util.List;

public interface BillService {
    BillDto createBill(BillDto billDto);
    BillDto updateBill(Long id, BillDto billDto);
    void deleteBill(Long id);
    BillDto getBillById(Long id);
    List<BillDto> getAllBills(String sortBy);
    List<BillDto> searchBills(String query);
}
