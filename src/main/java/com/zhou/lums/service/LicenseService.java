package com.zhou.lums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhou.lums.payload.ApiResponse;
import com.zhou.lums.respository.LicenseRepository;

@Service
@Transactional
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;
    public ResponseEntity<?> changeLicenseActive(long licenseId, boolean newActive) {
//        License license = licenseRepository.findById(licenseId)
//                .orElseThrow(() -> new ResourceNotFoundException("License", "id", licenseId));
//        license.setActive(newActive);
//        licenseRepository.save(license);
        licenseRepository.updateLicenseActive(newActive, licenseId);
        return ResponseEntity.ok(new ApiResponse(true, "Change License Active"));
    }

}
