package com.zhou.lums.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.lums.model.Log;
import com.zhou.lums.respository.LogRepository;

@RestController
@RequestMapping("/api/log")
public class LogController {
    @Autowired
    private LogRepository logRepository;

    @GetMapping
    public List<Log> listAlllogs() {
        return logRepository.findAllOrderedByTimeDesc();
    }

    @GetMapping("/by_user")
    public List<Log> listAllLogsByUser( @RequestParam("user_id") long userId) {
        return logRepository.findAllLogByUserIdLasestOrder(userId);
    }

    @GetMapping("/by_user_and_license")
    public List<Log> listAllLogsByUserAndLicense(
            @RequestParam("user_id") long userId,
            @RequestParam("license_id") long licenseId) {
        return logRepository.findAllLogByUserIdAndLicenseId(userId, licenseId);
    }
}
