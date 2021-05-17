package com.zhou.lums.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.License;
import com.zhou.lums.model.Sale;
import com.zhou.lums.model.User;
import com.zhou.lums.payload.ApiResponse;
import com.zhou.lums.respository.LicenseRepository;
import com.zhou.lums.respository.SaleRepository;
import com.zhou.lums.respository.UserRepository;
import com.zhou.lums.security.UserPrincipal;

@Service
@Transactional
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private LogService logService;

    public ResponseEntity<?> changeLicenseActive(long licenseId, boolean newActive) {
        if (licenseRepository.updateLicenseActive(newActive, licenseId) == 0) throw new ResourceNotFoundException("License", "id", licenseId);
        return ResponseEntity.ok(new ApiResponse(true, "Change License Active"));
    }

    public ResponseEntity<?> setLicensePrice(long licenseId, double price) {
      License license = licenseRepository.findById(licenseId)
              .orElseThrow(() -> new ResourceNotFoundException("License", "id", licenseId));
      license.setPrice(price);
      licenseRepository.save(license);
      return ResponseEntity.ok(new ApiResponse(true, "Set License Price"));
  }

    public ResponseEntity<?> purChaseLicense(UserPrincipal currentUser, long licenseId) {
        User user = userRepository.findById(currentUser.getId()).get();
        if (user.isBlocked()) {
            return new ResponseEntity<>(new ApiResponse(false, "User has been blocked"), HttpStatus.BAD_REQUEST);
        }
        License license = licenseRepository.findById(licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("License", "id", licenseId));
        Sale sale = new Sale();
        sale.setActive(true);
        sale.setPurchasedDate(LocalDate.now());
        sale.setUser(user);
        sale.setLicense(license);
        saleRepository.save(sale);
        logService.logPurchase(user, license);
        return ResponseEntity.ok(new ApiResponse(true, "Purchase License"));
    }

}
