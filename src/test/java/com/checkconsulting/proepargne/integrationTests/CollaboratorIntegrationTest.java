package com.checkconsulting.proepargne.integrationTests;

import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorOutDto;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CollaboratorIntegrationTest {

    @LocalServerPort
    Integer port;

    TestRestTemplate restTemplate;

    CollaboratorRepository collaboratorRepository;

    @Autowired
    public CollaboratorIntegrationTest(TestRestTemplate restTemplate, CollaboratorRepository collaboratorRepository) {
        this.restTemplate = restTemplate;
        this.collaboratorRepository = collaboratorRepository;
    }

    @BeforeEach
    public void initDB() {
        collaboratorRepository.deleteAll();
    }

    @Test
    public void itShouldCreateNewCollaborator() {
        //Given
        CollaboratorInDto collaboratorInDto = CollaboratorInDto
                .builder()
                .lastName("John")
                .firstName("Doe")
                .email("john.doe@gmail.com")
                .gender("homme")
                .birthDate(LocalDate.of(1988, 11, 11))
                .entryDate(LocalDate.of(2020, 5, 5))
                .grossSalary(2000)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CollaboratorInDto> requestEntity = new HttpEntity<>(collaboratorInDto, headers);

        //When
        ResponseEntity<CollaboratorOutDto> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/collaborator", requestEntity, CollaboratorOutDto.class);

        log.info("response Entity {}", responseEntity);
        //Then
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(collaboratorInDto.getEmail(), responseEntity.getBody().getEmail());
    }

    @Test
    public void itShouldNotCreateCollaboratorAndShouldReturnBadRequest() {
        //Given
        CollaboratorInDto collaboratorInDto = CollaboratorInDto
                .builder()
                .lastName("")
                .firstName("")
                .email("john.doe@gmail.com")
                .gender("homme")
                .birthDate(LocalDate.of(1988, 11, 11))
                .entryDate(LocalDate.of(2020, 5, 5))
                .grossSalary(2000)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CollaboratorInDto> requestEntity = new HttpEntity<>(collaboratorInDto, headers);

        //When
        ResponseEntity<Collaborator> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/collaborator", requestEntity, Collaborator.class);

        //Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
