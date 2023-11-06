package com.yb.empik.task.interview;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
@TestConfiguration(proxyBeanMethods = false)
public class TestInterviewApplication {

    public static final String USER_INFO_TEST_URL = "http://localhost:8080/api/octocat";

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
    }

    public static void main(String[] args) {
        SpringApplication.from(InterviewApplication::main).with(TestInterviewApplication.class).run(args);
        testGetUserData();
    }

    public static void testGetUserData() {
        RequestTest requestTest = new RequestTest();
        requestTest.requestTest();
    }

    public static class RequestTest {

        private final RestTemplate restTemplate;

        public RequestTest() {
            restTemplate = new RestTemplate();
        }

        @Test
        public void requestTest() {
            ExecutorService executorService = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 5; i++) {
                executorService.execute(() -> {
                    ResponseEntity<String> response = restTemplate.getForEntity(USER_INFO_TEST_URL, String.class);
                    assertEquals(200, response.getStatusCode().value());
                });
            }
        }
    }

}
