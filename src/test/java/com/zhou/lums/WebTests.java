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
import com.zhou.lums.payload.LoginRequest;
import com.zhou.lums.payload.UserSummary;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("unchecked")
public class WebTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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
        Map<String, Object> response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/signin",
                request, Map.class);
        token = (String) response.get("accessToken");
    }

    @Test
    public void getUsers() throws Exception {
        System.out.println("token is " + token);
        ResponseEntity<UserSummary[]> response = restTemplate.getForEntity("http://localhost:" + port + "/api/users",
                UserSummary[].class);
        for (UserSummary user : response.getBody()) {
            System.out.println(user.getId());
            System.out.println(user.getRole());
            System.out.println(user.getName());
        }

    }

    @Test
    public void getMe() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<UserSummary> response =restTemplate.exchange("http://localhost:" + port + "/api/user/me", HttpMethod.GET, request, UserSummary.class);
        System.out.println(response.getBody());

    }

    @Test
    public void changeRoleFail() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<?> response =restTemplate
                .exchange("http://localhost:" + port + "/api/users/1/modify_role?newRole=ROLE_USER",
                        HttpMethod.POST, request, String.class);
        assertTrue(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));

    }


}
