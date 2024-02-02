package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.UserDTO;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CollaboratorService {

    final CollaboratorRepository collaboratorRepository;
    final KeycloakUserService keycloakUserService;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, KeycloakUserService keycloakUserService) {
        this.collaboratorRepository = collaboratorRepository;
        this.keycloakUserService = keycloakUserService;
    }

    @Transactional
    public Collaborator createCollaborator(CollaboratorInDto collaboratorInDto) {
        log.info("Start persisting new Collaborator {}", collaboratorInDto);

        Collaborator collaborator = Collaborator.builder()
                .lastName(collaboratorInDto.getLastName())
                .firstName(collaboratorInDto.getFirstName())
                .gender(collaboratorInDto.getGender())
                .email(collaboratorInDto.getEmail())
                .grossSalary(collaboratorInDto.getGrossSalary())
                .birthDate(collaboratorInDto.getBirthDate())
                .entryDate(collaboratorInDto.getEntryDate())
                .build();

        UserDTO userDTO = UserDTO.builder()
                .emailId(collaboratorInDto.getEmail())
                .lastName(collaboratorInDto.getLastName())
                .firstname(collaboratorInDto.getFirstName())
                .userName(collaboratorInDto.getFirstName())
                .password("password123")
                .build();


        this.keycloakUserService.addUser(userDTO);
        return collaboratorRepository.save(collaborator);
    }
}
