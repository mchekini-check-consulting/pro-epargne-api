package com.checkconsulting.proepargne.unitTests;

import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.service.CollaboratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CollaboratorUnitTest {
    @InjectMocks
    private CollaboratorService collaboratorService;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Test
    public void itShouldCreateNewCollaborator() {
        //GIVEN
        CollaboratorInDto collaboratorInDto = CollaboratorInDto
                .builder()
                .lastName("John")
                .firstName("Doe")
                .email("john.doe@gmail.com")
                .gender("homme")
                .birthDate(LocalDate.of(1985, 1, 10))
                .entryDate(LocalDate.of(2021, 4, 5))
                .grossSalary(2000)
                .build();

        Collaborator collaborator = Collaborator
                .builder()
                .lastName("John")
                .firstName("Doe")
                .email("john.doe@gmail.com")
                .gender("homme")
                .birthDate(LocalDate.of(1980, 9, 3))
                .entryDate(LocalDate.of(2023, 1, 12))
                .build();

        //WHEN
        when(collaboratorRepository.save(collaborator)).thenReturn(collaborator);

        Collaborator response = collaboratorService.createCollaborator(collaboratorInDto);

        //THEN
        assertEquals(collaboratorInDto.getLastName(), response.getLastName());
        assertEquals(collaboratorInDto.getFirstName(), response.getFirstName());
        assertEquals(collaboratorInDto.getBirthDate(), response.getBirthDate());
    }
}
