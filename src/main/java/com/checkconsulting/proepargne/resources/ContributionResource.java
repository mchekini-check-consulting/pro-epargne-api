package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.dto.contribution.ContributionOutDto;
import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.service.ContributionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/contribution")
public class ContributionResource {
    private final ContributionService contributionService;

    public ContributionResource(ContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @GetMapping
    @Authenticated(authenticated = true)
    public ResponseEntity<Page<TransactionOutDto>> findAllContribution(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "", required = false) PlanType planType
    ){
        return ResponseEntity.ok().body(contributionService.findAllContribution(page,size,planType));
    }

    @PatchMapping(path = "{id}")
    @Authenticated(authenticated = true)
    public ResponseEntity<ContributionOutDto> updateContribution(@PathVariable Long id) throws GlobalException {
        return ResponseEntity.ok().body(contributionService.updateContributionStatus(id));
    }
}
