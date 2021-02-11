package com.zhou.lums.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhou.lums.model.Log;
import com.zhou.lums.model.User;
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
}
