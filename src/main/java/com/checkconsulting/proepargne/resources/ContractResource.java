package com.checkconsulting.proepargne.resources;


import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.service.ContractService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/contracts")
public class ContractResource {

    private final ContractService contractService;

    public ContractResource(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/{contractId}")
    public ContractOutDto getContract(@PathVariable Long contractId) throws GlobalException {
        return contractService.getContract(contractId);
    }

}
