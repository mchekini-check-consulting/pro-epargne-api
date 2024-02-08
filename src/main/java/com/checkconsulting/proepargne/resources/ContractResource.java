package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.dto.contract.ContractInDto;
import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.service.ContractService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @GetMapping
    public ContractOutDto getContract() throws GlobalException {
        return contractService.getContract();
    }

    @PostMapping
    public ResponseEntity<?> createContract(@RequestBody @Valid ContractInDto contractInDto) {

        // if (result.hasErrors()) {
        // System.out.println("hereherehere");
        // return ResponseEntity.badRequest().body(result.getAllErrors());
        // }
        return ResponseEntity.ok().build();
        // Contract contract = contractService.createContract(contractInDto);

        // URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        // .path("/{id}")
        // .buildAndExpand(contract.getContractId())
        // .toUri();

        // return ResponseEntity.created(location).build();
    }

}
