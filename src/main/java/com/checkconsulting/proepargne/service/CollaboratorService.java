package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.dto.UserDTO;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorOutDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorUpdateDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.CollaboratorMapper;
import com.checkconsulting.proepargne.model.*;
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
    final AccountService accountService;
    final ContractService contractService;
    private final User user;

    public CollaboratorService(CollaboratorRepository collaboratorRepository, KeycloakUserService keycloakUserService,
            CollaboratorMapper collaboratorMapper, AccountService accountService, ContractService contractService,
            User user) {
        this.collaboratorRepository = collaboratorRepository;
        this.keycloakUserService = keycloakUserService;
        this.collaboratorMapper = collaboratorMapper;
        this.accountService = accountService;
        this.contractService = contractService;
        this.user = user;

    }
    @Authenticated(authenticated = true)
    public List<CollaboratorOutDto> getAll() {
        List<Collaborator> collaborators = collaboratorRepository.findAllCompanyAdminCollaborators(user.getKeycloakId());
        return collaborators.stream().map(collaboratorMapper::mapToCollaboratorOutDto).collect(Collectors.toList());
    }

    @Transactional
    public CollaboratorOutDto createCollaborator(CollaboratorInDto collaboratorInDto) throws GlobalException,EntityExistsException{
        log.info("Start adding new Collaborator {}", collaboratorInDto);

        Optional<Collaborator> existedCollaborator = collaboratorRepository.findByEmail(collaboratorInDto.getEmail());

        if (existedCollaborator.isPresent()) throw new EntityExistsException("Employe existe deja");

        String userName = KeycloakUserNameHandler.generateUserName(collaboratorInDto.getFirstName(), collaboratorInDto.getLastName());
        UserDTO userDTO = UserDTO.builder()
                .email(collaboratorInDto.getEmail())
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

        log.info("Saving new Collaborator to database and flushing changes");
        collaboratorRepository.saveAndFlush(collaborator);

        accountService.createCollaboratorAccounts(collaborator);

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
                .email(collaboratorUpdateDto.getEmail())
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
