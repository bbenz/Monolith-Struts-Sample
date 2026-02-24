package com.skishop.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ActuatorIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    private ResponseEntity<String> get(String path) {
        return restTemplate.getForEntity(url(path), String.class);
    }

    @Test
    void health_isUp() {
        ResponseEntity<String> resp = get("/actuator/health");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("UP");
    }

    @Test
    void metrics_isExposed() {
        ResponseEntity<String> resp = get("/actuator/metrics");
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("http.server.requests");
    }

    @Test
    void prometheus_isExposed() {
        ResponseEntity<String> root = get("/actuator");
        assertThat(root.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(root.getBody()).contains("prometheus");

        ResponseEntity<String> resp = get("/actuator/prometheus");
        assertThat(resp.getStatusCode())
                .as("prometheus status body=%s", resp.getBody())
                .isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("# HELP");
    }
}
