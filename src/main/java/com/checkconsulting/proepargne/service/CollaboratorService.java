package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.UserDTO;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorUpdateDto;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.utils.KeycloakUserNameHandler;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Optional<Collaborator> existedCollaborator = collaboratorRepository.findByEmail(collaboratorInDto.getEmail());

        if (existedCollaborator.isPresent()) throw new EntityExistsException("Employe existe deja");

        String userName = KeycloakUserNameHandler.generateUserName(collaboratorInDto.getFirstName(), collaboratorInDto.getLastName());
        UserDTO userDTO = UserDTO.builder()
                .emailId(collaboratorInDto.getEmail())
                .lastName(collaboratorInDto.getLastName())
                .firstname(collaboratorInDto.getFirstName())
                .userName(userName)
                .build();

        String userId = keycloakUserService.addUser(userDTO);

        Collaborator collaborator = Collaborator.builder()
                .lastName(collaboratorInDto.getLastName())
                .firstName(collaboratorInDto.getFirstName())
                .gender(collaboratorInDto.getGender())
                .email(collaboratorInDto.getEmail())
                .grossSalary(collaboratorInDto.getGrossSalary())
                .birthDate(collaboratorInDto.getBirthDate())
                .entryDate(collaboratorInDto.getEntryDate())
                .keycloakId(userId)
                .build();

        return collaboratorRepository.save(collaborator);
    }

    @Transactional
    public Collaborator updateCollaborator(Long id, CollaboratorUpdateDto collaboratorUpdateDto) {
        log.info("Start updating Collaborator with new data : {}", collaboratorUpdateDto);

        Optional<Collaborator> existedCollaborator = collaboratorRepository.findByEmail(collaboratorUpdateDto.getEmail());
        if (existedCollaborator.isPresent() && !existedCollaborator.get().getId().equals(id)) {
            throw new EntityExistsException("L'email existe déja");
        }

        Collaborator collaborator = collaboratorRepository.findById(collaboratorUpdateDto.getId()).orElseThrow(() -> new EntityNotFoundException("L'employe n'existe pas"));

        collaborator.setLastName(collaboratorUpdateDto.getLastName());
        collaborator.setFirstName(collaboratorUpdateDto.getFirstName());
        collaborator.setGender(collaboratorUpdateDto.getGender());
        collaborator.setEmail(collaboratorUpdateDto.getEmail());
        collaborator.setGrossSalary(collaboratorUpdateDto.getGrossSalary());
        collaborator.setBirthDate(collaboratorUpdateDto.getBirthDate());
        collaborator.setEntryDate(collaboratorUpdateDto.getEntryDate());

        String userName = KeycloakUserNameHandler.generateUserName(collaboratorUpdateDto.getFirstName(), collaboratorUpdateDto.getLastName());

        UserDTO userDTO = UserDTO.builder()
                .emailId(collaboratorUpdateDto.getEmail())
                .lastName(collaboratorUpdateDto.getLastName())
                .firstname(collaboratorUpdateDto.getFirstName())
                .userName(userName)
                .build();

        keycloakUserService.updateUser(collaborator.getKeycloakId(), userDTO);
        return collaboratorRepository.save(collaborator);
    }
}
