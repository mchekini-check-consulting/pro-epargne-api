package com.checkconsulting.proepargne.unitTests;

import com.checkconsulting.proepargne.dto.UserDTO;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorOutDto;
import com.checkconsulting.proepargne.mapper.CollaboratorMapper;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.service.CollaboratorService;
import com.checkconsulting.proepargne.service.KeycloakUserService;
import com.checkconsulting.proepargne.utils.KeycloakUserNameHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollaboratorUnitTest {
    @InjectMocks
    private CollaboratorService collaboratorService;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private KeycloakUserService keycloakUserService;

    @Mock
    private CollaboratorMapper collaboratorMapper;

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

        CollaboratorOutDto collaboratorOutDto = CollaboratorOutDto
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
                .birthDate(LocalDate.of(1985, 1, 10))
                .entryDate(LocalDate.of(2021, 4, 5))
                .grossSalary(2000)
                .keycloakId("123456")
                .build();

        String userName = KeycloakUserNameHandler.generateUserName(collaboratorInDto.getFirstName(), collaboratorInDto.getLastName());

        UserDTO userDTO = UserDTO
                .builder()
                .email(collaboratorInDto.getEmail())
                .firstname(collaboratorInDto.getFirstName())
                .lastName(collaboratorInDto.getLastName())
                .userName(userName).build();

        //WHEN
        when(collaboratorRepository.findByEmail(collaboratorInDto.getEmail())).thenReturn(Optional.empty());
        when(collaboratorRepository.save(collaborator)).thenReturn(collaborator);
        when(keycloakUserService.addUser(userDTO)).thenReturn(collaborator.getKeycloakId());
        when(collaboratorMapper.mapToCollaboratorOutDto(collaboratorRepository.save(collaborator))).thenReturn(collaboratorOutDto);

        CollaboratorOutDto response = collaboratorService.createCollaborator(collaboratorInDto);

        //THEN
        assertEquals(collaboratorInDto.getLastName(), response.getLastName());
        assertEquals(collaboratorInDto.getFirstName(), response.getFirstName());
        assertEquals(collaboratorInDto.getBirthDate(), response.getBirthDate());
    }
}
