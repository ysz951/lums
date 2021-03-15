package com.zhou.lums;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import com.zhou.lums.model.License;
import com.zhou.lums.model.License.Duration;
import com.zhou.lums.payload.LoginRequest;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("unchecked")
public class LicenseTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String token;

    @BeforeEach
    public void loginTest() {
        LoginRequest login = new LoginRequest();
        HttpEntity<LoginRequest> request = new HttpEntity<>(login);
        login.setUsernameOrEmail("lums@admin.com");
        login.setPassword("admin123");
        Map<String, ?> response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/signin",
                request, Map.class);
        token = (String) response.get("accessToken");
        System.out.println(response.get("accessToken"));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        License license = new License();
        license.setActive(true);
        license.setDuration(Duration.MONTHLY);
        license.setPrice(10.5);
        license.setYear(2013);
        HttpEntity<License> licenseRequest = new HttpEntity<>(license, headers);

        restTemplate.exchange("http://localhost:" + port + "/api/license", HttpMethod.POST, licenseRequest, String.class);
    }

    @Test
    public void getLicense() throws Exception {
        System.out.println("license");
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


}
