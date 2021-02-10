package com.zhou.lums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhou.lums.respository.LicenseRepository;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;
}
