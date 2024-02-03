package com.checkconsulting.proepargne.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkconsulting.proepargne.dto.contract.ContractInDTO;
import com.checkconsulting.proepargne.services.ContractService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("api/v1/contract")
@RequiredArgsConstructor
public class ContractResource {

    private final ContractService contractService;

    @PostMapping()
    public ResponseEntity<?> createContract(@RequestBody ContractInDTO contract) {
        var contractRes = contractService.createContract(contract);
        return ResponseEntity.ok().body(contractRes);
    }
}
