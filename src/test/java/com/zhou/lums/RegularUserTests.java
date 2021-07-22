package com.zhou.lums;

import com.zhou.lums.payload.LoginRequest;
import com.zhou.lums.payload.PasswordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegularUserTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    private String testUser = "lums@user.com";

    @Value("${test.password.user}")
    private String testUserPassword;

    @BeforeEach
    public void loginTest() {
        LoginRequest login = new LoginRequest();
        HttpEntity<LoginRequest> request = new HttpEntity<>(login);
        login.setUsernameOrEmail(testUser);
        login.setPassword(testUserPassword);
        Map<String, Object> response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/signin",
                request, Map.class);
        token = (String) response.get("accessToken");
    }

    @Test
    public void changeRoleFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("http://localhost:" + port + "/api/users/1/modify_role?newRole=ROLE_ADMIN", HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void changeEmailFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("http://localhost:" + port + "/api/users/email/1?new_email=change@lums.com", HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void changePasswordFail() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setOldPassword("old");
        passwordRequest.setNewPassword("new");
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<PasswordRequest> request = new HttpEntity<>(passwordRequest, headers);
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("http://localhost:" + port + "api/users/password?user_id=1", HttpMethod.POST, request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

}
