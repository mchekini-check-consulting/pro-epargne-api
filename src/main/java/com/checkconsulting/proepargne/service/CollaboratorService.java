package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.UserDTO;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorOutDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorUpdateDto;
import com.checkconsulting.proepargne.mapper.CollaboratorMapper;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.utils.KeycloakUserNameHandler;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CollaboratorService {

    final CollaboratorRepository collaboratorRepository;
    final KeycloakUserService keycloakUserService;
    final CollaboratorMapper collaboratorMapper;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, KeycloakUserService keycloakUserService, CollaboratorMapper collaboratorMapper) {
        this.collaboratorRepository = collaboratorRepository;
        this.keycloakUserService = keycloakUserService;
        this.collaboratorMapper = collaboratorMapper;
    }

    public List<CollaboratorOutDto> getAll() {
        List<Collaborator> collaborators = collaboratorRepository.findAll();
        return collaborators.stream().map(collaborator -> collaboratorMapper.mapToCollaboratorOutDto(collaborator)).collect(Collectors.toList());
    }

    @Transactional
    public CollaboratorOutDto createCollaborator(CollaboratorInDto collaboratorInDto) {
        log.info("Start adding new Collaborator {}", collaboratorInDto);

        Optional<Collaborator> existedCollaborator = collaboratorRepository.findByEmail(collaboratorInDto.getEmail());

        if (existedCollaborator.isPresent()) throw new EntityExistsException("Employe existe deja");

        String userName = KeycloakUserNameHandler.generateUserName(collaboratorInDto.getFirstName(), collaboratorInDto.getLastName());
        UserDTO userDTO = UserDTO.builder()
                .emailId(collaboratorInDto.getEmail())
                .lastName(collaboratorInDto.getLastName())
                .firstname(collaboratorInDto.getFirstName())
                .userName(userName)
                .build();

        log.info("Start adding new Collaborator in keycloak");
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

        return collaboratorMapper.mapToCollaboratorOutDto(collaboratorRepository.save(collaborator));
    }

    @Transactional
    public CollaboratorOutDto updateCollaborator(Long id, CollaboratorUpdateDto collaboratorUpdateDto) {
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

        log.info("Start updating Collaborator in keycloak");
        keycloakUserService.updateUser(collaborator.getKeycloakId(), userDTO);
        log.info("Start updating Collaborator in database");
        return collaboratorMapper.mapToCollaboratorOutDto(collaboratorRepository.save(collaborator));
    }
}
