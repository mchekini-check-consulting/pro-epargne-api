package com.checkconsulting.proepargne.service;


import com.checkconsulting.proepargne.dto.requestWithdrawal.RequestWithdrawalDto;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.RequestWithdrawal;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.repository.RequestWithdrawalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RequestWithdrawalService {

    private final RequestWithdrawalRepository requestWithdrawalRepository;
    final CollaboratorRepository collaboratorRepository;
    private final User user;
//    private final StorageService storageService; // todo :ajouter les cles pour s3 dans vault : décommenter les class StorageConfig et StorageService
    public RequestWithdrawalService(RequestWithdrawalRepository requestWithdrawalRepository, CollaboratorRepository collaboratorRepository, User user) {
        this.requestWithdrawalRepository = requestWithdrawalRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.user = user;

    }


    public List<RequestWithdrawal> saveRequestWithdrawal(List<RequestWithdrawalDto> requestWithdrawalDtos, MultipartFile filePee ,MultipartFile filePereco) throws GlobalException {
        List<RequestWithdrawal> requestWithdrawals = new ArrayList<>();
        for (RequestWithdrawalDto r: requestWithdrawalDtos) {
            RequestWithdrawal requestWithdrawal = createRequestWithdrawal(r);

            // todo : ajouter le s3
//            if (r.getTypeAccount().equals(PlanType.PEE) && !filePee.isEmpty()){
//                storageService.uploadFile(filePee);
//            } else if (r.getTypeAccount().equals(PlanType.PERECO) && !filePereco.isEmpty()){
//                storageService.uploadFile(filePereco);
//            }
            requestWithdrawals.add(requestWithdrawal);
        }
        return requestWithdrawals;
    }



    public RequestWithdrawal createRequestWithdrawal(RequestWithdrawalDto requestWithdrawalDto) {

        Collaborator collaborator = collaboratorRepository.findByEmail(user.getEmail()).orElseThrow(() ->
                new RuntimeException("Collaborator not found for email: " + user.getEmail()));

        log.info("Creating request withdrawal for collaborator: {}", collaborator.getId());
        RequestWithdrawal requestWithdrawal = new RequestWithdrawal();
        requestWithdrawal.setCollaborator(collaborator);
        requestWithdrawal.setCreatedAt(LocalDateTime.now());
        requestWithdrawal.setAmount(requestWithdrawalDto.getAmount());
        requestWithdrawal.setRib(requestWithdrawalDto.getRib());
        requestWithdrawal.setReasonUnblocking(requestWithdrawalDto.getReasonUnblocking());
        requestWithdrawal.setTypeAccount(PlanType.valueOf(requestWithdrawalDto.getTypeAccount()));


        requestWithdrawal = requestWithdrawalRepository.save(requestWithdrawal);

        log.info("Request withdrawal saved successfully with ID: {}", requestWithdrawal.getId());

        return requestWithdrawal;
    }
}
