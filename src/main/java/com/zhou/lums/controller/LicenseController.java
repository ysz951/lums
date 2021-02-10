package com.zhou.lums.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.lums.model.License;
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
}
