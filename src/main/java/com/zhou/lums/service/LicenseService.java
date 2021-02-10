package com.zhou.lums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.License;
import com.zhou.lums.payload.ApiResponse;
import com.zhou.lums.respository.LicenseRepository;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;
    public ResponseEntity<?> changeLicenseActive(long licenseId, boolean newActive) {
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("License", "id", licenseId));
        license.setActive(newActive);
        licenseRepository.save(license);
        return ResponseEntity.ok(new ApiResponse(true, "Change sale exipration"));
    }
 //    public ResponseEntity<?> changeSaleExpiration(long saleId, LocalDate expireDate) {
//        Sale sale = saleRepository
//                .findById(saleId)
//                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", saleId));
//        sale.setExpireDate(expireDate);
//        saleRepository.save(sale);
//        return ResponseEntity.ok(new ApiResponse(true, "Change sale exipration"));
//    }
}
