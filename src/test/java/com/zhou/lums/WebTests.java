package com.zhou.lums;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import com.zhou.lums.payload.LoginRequest;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SuppressWarnings("unchecked")
public class WebTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static String token;

    @Value("${test_user}")
    private String testUser;

    @Value("${test_user_password}")
    private String testUserPassword;

    @BeforeEach
    public void loginTest() {
        LoginRequest login = new LoginRequest();
        HttpEntity<LoginRequest> request = new HttpEntity<>(login);
        login.setUsernameOrEmail(testUser);
        login.setPassword(testUserPassword);
        Map<String, ?> response = restTemplate.postForObject("http://localhost:" + port + "/api/auth/signin",
                request, Map.class);
        token = (String) response.get("accessToken");
        System.out.println(response.get("accessToken"));
    }

//    @Test
//    public void greetingShouldReturnDefaultMessage() throws Exception {
//        System.out.println("token is " + token);
//        List<Map<String, ?>> response = restTemplate.getForObject("http://localhost:" + port + "/api/users",
//                List.class);
//        int i = 1;
//        for (Map user : response) {
//            System.out.println((int) user.get("id") == i);
//            System.out.println(user.get("email"));
//            System.out.println(user.get("blocked"));
//            i++;
//        }
//        System.out.println(response.size());
//
//    }

    @Test
    public void getLicense() throws Exception {
        System.out.println("license");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response =restTemplate.exchange("http://localhost:" + port + "/api/user/me", HttpMethod.GET, request, Map.class);
        System.out.println(response.getBody());
        Map<String, ?> ob = response.getBody();
        for (String key : ob.keySet()) {
            System.out.println(key + " : " + ob.get(key));
        }
    }


}
