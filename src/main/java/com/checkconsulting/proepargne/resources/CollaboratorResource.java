package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.dto.collaborator.CollaboratorInDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorOutDto;
import com.checkconsulting.proepargne.dto.collaborator.CollaboratorUpdateDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.service.CollaboratorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/collaborator")
public class CollaboratorResource {
    final CollaboratorService collaboratorService;

    public CollaboratorResource(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @GetMapping
    public ResponseEntity<List<CollaboratorOutDto>> getAll(){
        return new ResponseEntity<>(collaboratorService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CollaboratorOutDto> createCollaborator(@Valid @RequestBody CollaboratorInDto collaboratorInDto) throws GlobalException{
        return new ResponseEntity<>(collaboratorService.createCollaborator(collaboratorInDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollaboratorOutDto> updateCollaborator(@PathVariable("id") Long id, @Valid @RequestBody CollaboratorUpdateDto collaboratorUpdateDto) {
        return new ResponseEntity<>(collaboratorService.updateCollaborator(id, collaboratorUpdateDto), HttpStatus.OK);
    }
}
