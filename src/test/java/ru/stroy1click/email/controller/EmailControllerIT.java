package ru.stroy1click.email.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.stroy1click.email.dto.UserDto;
import ru.stroy1click.email.dto.Role;
import ru.stroy1click.email.dto.SendEmailRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmailControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private UserDto validUser;

    @BeforeEach
    void setUp() {
        this.validUser = UserDto.builder()
                .email("test@domain.com")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .emailConfirmed(true)
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void sendEmail_WithValidRequest_ReturnsOk() {
        SendEmailRequest request = SendEmailRequest.builder()
                .user(this.validUser)
                .code(1234567)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SendEmailRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/emails/send", entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Электронное письмо успешно отправлено"));
    }

    @Test
    void sendEmail_WithInvalidCode_ReturnsBadRequest() {
        SendEmailRequest request = SendEmailRequest.builder()
                .user(this.validUser)
                .code(999) // меньше 1_000_000
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SendEmailRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = this.restTemplate.postForEntity("/api/v1/emails/send", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Значение кода должно быть в диапазоне от 1.000.000 до 9.999.999"));
    }

    @Test
    void sendEmail_WithEmptyEmail_ReturnsBadRequest() {
        UserDto invalidUser = UserDto.builder()
                .email("")
                .firstName("John")
                .lastName("Doe")
                .password("password123")
                .emailConfirmed(true)
                .role(Role.ROLE_USER)
                .build();

        SendEmailRequest request = SendEmailRequest.builder()
                .user(invalidUser)
                .code(1234567)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SendEmailRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/emails/send", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Электронная почта не может быть пустой"));
    }

    @Test
    void sendEmail_WithNullUser_ReturnsBadRequest() {
        SendEmailRequest request = SendEmailRequest.builder()
                .user(null)
                .code(1234567)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SendEmailRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/emails/send", entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Данные пользователя не могу быть пустыми"));
    }
}
