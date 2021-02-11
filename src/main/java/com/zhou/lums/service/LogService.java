package com.zhou.lums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhou.lums.model.License;
import com.zhou.lums.model.Log;
import com.zhou.lums.model.User;
import com.zhou.lums.model.User.Role;
import com.zhou.lums.respository.LogRepository;

@Service
@Transactional
public class LogService {

    @Autowired
    private LogRepository logRepository;

    public void logBlockUser(User admin, User user, boolean isBlocked) {
        Log log = new Log();
        log.setUser(user);
        log.setAdmin(admin);
        if (isBlocked) {
            log.setLog("Blocked");
        } else {
            log.setLog("Unblocked");
        }
        logRepository.save(log);
    }

    public void logModifyRole(User admin, User user, Role prevRole, Role newRole) {
        Log log = new Log();
        log.setUser(user);
        log.setAdmin(admin);
        log.setPrevRole(prevRole);
        log.setNewRole(newRole);
        logRepository.save(log);
    }

    public void logPurchase(User user, License license){
        Log log = new Log();
        log.setUser(user);
        log.setLicense(license);
        logRepository.save(log);
    }
}
