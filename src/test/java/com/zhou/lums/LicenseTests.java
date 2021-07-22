package com.zhou.lums;

import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;
import com.zhou.lums.payload.LoginRequest;
import com.zhou.lums.respository.LicenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("unchecked")
public class LicenseTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LicenseRepository licenseRepository;

    private String token;

    private String testAdmin = "lums@admin.com";

    @Value("${test.password.admin}")
    private String testAdminPassword;

    @BeforeEach
    public void loginTest() {
        LoginRequest login = new LoginRequest();
        HttpEntity<LoginRequest> request = new HttpEntity<>(login);
        login.setUsernameOrEmail(testAdmin);
        login.setPassword(testAdminPassword);
        Map<String, ?> response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/signin",
                request, Map.class);
        token = (String) response.get("accessToken");
    }

    @Test
    public void getLicense() throws Exception {
        License l = new License();
        l.setActive(true);
        l.setDuration(Duration.MONTHLY);
        l.setPrice(10.5);
        l.setYear(2013);
        licenseRepository.save(l);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<License[]> response = restTemplate.exchange("http://localhost:" + port + "/api/license", HttpMethod.GET, request, License[].class);
        System.out.println(response.getBody());
        License[] licenses = response.getBody();
        for (License license : licenses) {
            System.out.println(license.getId());
            System.out.println(license.getPrice());
            System.out.println(license.getDuration());
        }

    }

    @Test
    public void postLicenseFail() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        License license = new License();
        license.setActive(true);
        license.setDuration(Duration.MONTHLY);
        license.setPrice(-10);
        license.setYear(2013);
        HttpEntity<License> licenseRequest = new HttpEntity<>(license, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://localhost:" + port + "/api/license", HttpMethod.POST, licenseRequest, String.class);

        assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void postLicenseTrue() throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        License license = new License();
        license.setActive(true);
        license.setDuration(Duration.MONTHLY);
        license.setPrice(2);
        license.setYear(2013);
        HttpEntity<License> licenseRequest = new HttpEntity<>(license, headers);
        ResponseEntity<?> response = restTemplate.exchange("http://localhost:" + port + "/api/license", HttpMethod.POST, licenseRequest, String.class);
        System.out.println(response.getStatusCode());

        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

}
