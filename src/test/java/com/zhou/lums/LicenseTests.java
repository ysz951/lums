package com.zhou.lums;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;
import com.zhou.lums.payload.LoginRequest;
import com.zhou.lums.respository.LicenseRepository;


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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);

        //        HttpEntity<License> licenseRequest = new HttpEntity<>(license, headers);
        //
        //        restTemplate.exchange("http://localhost:" + port + "/api/license", HttpMethod.POST, licenseRequest, String.class);
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
        ResponseEntity<Map[]> response =restTemplate.exchange("http://localhost:" + port + "/api/license", HttpMethod.GET, request, Map[].class);
        System.out.println(response.getBody());
        Map<String, ?>[] licenses = response.getBody();
        for (Map<String, ?> license : licenses) {
            for (String key : license.keySet()) {
                System.out.println(key + " : " + license.get(key));
            }
        }

    }

    @Test
    public void postLicense() throws Exception {

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
        //        System.out.println(HttpStatus.OK);
        assertTrue(response.getStatusCode().equals(HttpStatus.OK));
    }

}
