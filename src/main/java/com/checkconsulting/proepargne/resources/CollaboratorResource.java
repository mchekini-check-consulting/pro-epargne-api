package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorUpdateDto;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.service.CollaboratorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;

@Slf4j
@RestController
@RequestMapping("api/v1/collaborator")
public class CollaboratorResource {
    final CollaboratorService collaboratorService;

    public CollaboratorResource(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @PostMapping
    public ResponseEntity<Collaborator> createCollaborator(@Valid @RequestBody CollaboratorInDto collaboratorInDto) {
        return new ResponseEntity<>(this.collaboratorService.createCollaborator(collaboratorInDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Collaborator> updateCollaborator(@PathVariable("id") Long id, @Valid @RequestBody CollaboratorUpdateDto collaboratorUpdateDto) {
        return new ResponseEntity<>(this.collaboratorService.updateCollaborator(id, collaboratorUpdateDto), HttpStatus.OK);
    }
}
