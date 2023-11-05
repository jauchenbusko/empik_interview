package com.yb.empik.task.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.Assert.assertEquals;

@TestConfiguration(proxyBeanMethods = false)
public class TestInterviewApplication {

	public static final String USER_INFO_TEST_URL = "http://localhost:8080/api/octocat";
	private static RestTemplate restTemplate;

    public TestInterviewApplication() {
        TestInterviewApplication.restTemplate = new RestTemplate();
    }

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
		ResponseEntity<String> response = restTemplate.getForEntity(USER_INFO_TEST_URL, String.class);
		assertEquals(200, response.getStatusCode().value());
	}

}
