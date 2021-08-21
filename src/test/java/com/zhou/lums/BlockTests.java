package com.zhou.lums;

import com.zhou.lums.model.User;
import com.zhou.lums.payload.LoginRequest;
import com.zhou.lums.respository.UserRepository;
import org.junit.jupiter.api.AfterEach;
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
public class BlockTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String token;

    private String testAdmin = "lums@admin.com";

    @Value("${test.password.admin}")
    private String testAdminPassword;

    private String testUser = "user";

    @BeforeEach
    public void loginTest() {
        LoginRequest login = new LoginRequest();
        HttpEntity<LoginRequest> request = new HttpEntity<>(login);
        login.setUsernameOrEmail(testAdmin);
        login.setPassword(testAdminPassword);
        Map<String, Object> response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/signin",
                request, Map.class);
        token = (String) response.get("accessToken");
    }

    @AfterEach
    public void resetTestUser() {
        User user = userRepository.findByUsername(testUser).get();
        user.setBlocked(false);
        userRepository.save(user);
    }

    @Test
    public void blockUser() {
        User user = userRepository.findByUsername(testUser).get();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/users/block/" + String.valueOf(user.getId()),
                HttpMethod.POST,
                request,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User afterChange = userRepository.findByUsername(testUser).get();
        assertEquals(true, afterChange.isBlocked());
    }

    @Test
    public void unblockUser() {
        User user = userRepository.findByUsername(testUser).get();
        user.setBlocked(true);
        userRepository.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/users/unblock/" + String.valueOf(user.getId()),
                HttpMethod.POST,
                request,
                String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User afterChange = userRepository.findByUsername(testUser).get();
        assertEquals(false, afterChange.isBlocked());
    }

}
