package com.zhou.lums;

import com.zhou.lums.payload.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class UserLoginTest {

    @Autowired
    public TestRestTemplate restTemplate;

    protected String token;
    @LocalServerPort
    protected int port;

    @Value("${test.password.user}")
    private String testUserPassword;

    private String testUser = "lums@user.com";

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
}
