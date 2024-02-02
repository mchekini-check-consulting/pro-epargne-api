package com.checkconsulting.proepargne.mapper;

import com.checkconsulting.proepargne.dto.collaborator.CollaboratorOutDto;
import com.checkconsulting.proepargne.model.Collaborator;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CollaboratorMapper {

    CollaboratorOutDto mapToCollaboratorOutDto(Collaborator collaborator);
}
