package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.dto.requestWithdrawal.RequestWithdrawalDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.RequestWithdrawal;
import com.checkconsulting.proepargne.service.RequestWithdrawalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequestMapping("/api/v1/requestWithdrawal")
public class RequestWithdrawalController {

    private final RequestWithdrawalService requestWithdrawalService;


    public RequestWithdrawalController(RequestWithdrawalService requestWithdrawalService) {
        this.requestWithdrawalService = requestWithdrawalService;
    }


    @PostMapping()
    public ResponseEntity<List<RequestWithdrawal>> createRequestWithdrawal(@RequestPart(value = "body") List<RequestWithdrawalDto> requestWithdrawalDto,
                                                                           @RequestPart(value = "filePee", required = false) MultipartFile filePee,
                                                                           @RequestPart(value = "filePerco", required = false) MultipartFile filePerco) throws GlobalException {
        List<RequestWithdrawal> requestWithdrawals = requestWithdrawalService.saveRequestWithdrawal(requestWithdrawalDto , filePee , filePerco);
        return new ResponseEntity<>(requestWithdrawals, HttpStatus.CREATED);
    }


}


