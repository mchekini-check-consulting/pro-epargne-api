package com.checkconsulting.proepargne.resources;


import com.checkconsulting.proepargne.dto.contract.ContractInDto;
import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/contract")
public class ContractResource {

    private final ContractService contractService;

    public ContractResource(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/{contractId}")
    public ContractOutDto getContract(@PathVariable Long contractId) throws GlobalException {
        return contractService.getContract(contractId);
    }

    @PostMapping
    public ResponseEntity createContract(@RequestBody ContractInDto contractInDto) {
        Contract contract = contractService.createContract(contractInDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(contract.getContractId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

}