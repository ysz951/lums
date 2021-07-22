package com.zhou.lums;

import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;
import com.zhou.lums.model.Log;
import com.zhou.lums.model.User;
import com.zhou.lums.respository.LicenseRepository;
import com.zhou.lums.respository.LogRepository;
import com.zhou.lums.respository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LumsApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    LicenseRepository licenseRepository;

    @Autowired
    LogRepository logRepository;

    @Test
    void contextLoads() {
    }

    @Test
    public void testCreateProduct() throws Exception {
        User user = new User();
        user.setName("first");
        user.setUsername("username1");
        user.setEmail("test1@lums.com");
        user.setPassword("password1");
        user.setBlocked(false);
        userRepository.save(user);
        User db_user = userRepository.findByEmail("test1@lums.com")
                .orElseThrow(() -> new Exception("User not found"));
        assertEquals(user.getName(), db_user.getName());

    }

    @Test
    public void testInValidName() {
        User user = new User();
        user.setName("first1");
        user.setUsername("username2");
        user.setEmail("test2@lums.com");
        user.setPassword("password1");
        user.setBlocked(false);
        try {
            userRepository.save(user);
            fail("should not fail");
        } catch (Exception ex) {
            assertTrue(true);
        }

    }

    @Test
    public void duplicateEmail() {
        User user = new User();
        user.setName("second");
        user.setUsername("username2");
        user.setEmail("test1@lums.com");
        user.setPassword("password1");
        user.setBlocked(false);
        try {
            userRepository.save(user);
            fail("should not fail");
        } catch (Exception ex) {
            assertTrue(true);
        }

    }

    @Test
    public void invalidPassword() {
        User user = new User();
        user.setName("third");
        user.setUsername("username3");
        user.setEmail("test3@lums.com");
        user.setPassword("ag");
        user.setBlocked(false);
        try {
            userRepository.save(user);
            fail("should not fail");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void createLog() {
        License license = new License();
        license.setActive(true);
        license.setDuration(Duration.YEARLY);
        license.setYear(2013);
        license.setPrice(20);
        licenseRepository.save(license);
        Log log = new Log();
        License dblicense = licenseRepository.findById((long) 1).get();
        log.setLicense(dblicense);
        logRepository.save(log);
        Log dbLog = logRepository.findById((long) 1).get();
        System.out.println(dbLog.getCreatedAt());
        assertTrue(true);
    }

    @Test
    public void createLicenseFail() {
        License license = new License();
        license.setActive(true);
        license.setDuration(Duration.YEARLY);
        license.setYear(2013);
        license.setPrice(-0.1);
        try {
            licenseRepository.save(license);
            fail("should not fail");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }


}
