package com.zhou.lums;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaleDeleteTest extends UserLoginTest {

    @Test
    public void deleteSaleUnLogin() {
        HttpEntity<?> request = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/sale/1",
                HttpMethod.DELETE, request,
                String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void deleteSaleLogin() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/sale/1",
                HttpMethod.DELETE, request,
                String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
