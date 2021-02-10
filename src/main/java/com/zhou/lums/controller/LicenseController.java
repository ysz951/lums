package com.zhou.lums.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;
import com.zhou.lums.respository.LicenseRepository;
import com.zhou.lums.service.LicenseService;

@RestController
@RequestMapping("/api/license")
public class LicenseController {

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private LicenseService licenseService;

    @PostMapping
    public License createLicense(@Valid @RequestBody License license) {
        return licenseRepository.save(license);
    }

    @GetMapping
    public List<License> listAllLicense() {
        return licenseRepository.findAll();
    }

    @GetMapping("/{licenseId}")
    public License listLicenseById(@PathVariable(value="licenseId") long licenseId) {
        return licenseRepository.findById(licenseId)
                .orElseThrow(() -> new ResourceNotFoundException("License", "id", licenseId));
    }


    @GetMapping("/duration")
    public List<License> listAllLicenseByDuration(@RequestParam(value = "duration") Duration duration) {
        return licenseRepository.findAllOrderedByIdFilterByType(duration);
    }

    @PutMapping("/active/{licenseId}/{newActive}")
    public ResponseEntity<?> changeLicenseActive (@PathVariable(value="licenseId") long licenseId,
            @PathVariable(value="newActive") boolean newActive) {
        return licenseService.changeLicenseActive(licenseId, newActive);
    }
}
