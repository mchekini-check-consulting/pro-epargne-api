package com.checkconsulting.proepargne.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.checkconsulting.proepargne.dto.contract.CompanyDTO;
import com.checkconsulting.proepargne.dto.contract.CompanySignatoryDTO;
import com.checkconsulting.proepargne.dto.contract.ContractInDTO;
import com.checkconsulting.proepargne.dto.contract.ContractOutDTO;
import com.checkconsulting.proepargne.dto.contract.ContractOutDTO.ContractOutDTOBuilder;
import com.checkconsulting.proepargne.dto.contract.PeeContributionDTO;
import com.checkconsulting.proepargne.dto.contract.PercoContributionDTO;
import com.checkconsulting.proepargne.model.Company;
import com.checkconsulting.proepargne.model.CompanySignatory;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.PeeContribution;
import com.checkconsulting.proepargne.model.PercoContribution;
import com.checkconsulting.proepargne.repository.CompanyRepository;
import com.checkconsulting.proepargne.repository.CompanySignatoryRepository;
import com.checkconsulting.proepargne.repository.ContractRepository;
import com.checkconsulting.proepargne.repository.PeeContributionRepository;
import com.checkconsulting.proepargne.repository.PercoContributionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {

        private final CompanyRepository companyRepository;
        private final ContractRepository contractRepository;
        private final CompanySignatoryRepository companySignatoryRepository;
        private final PercoContributionRepository perecoContributionRepository;
        private final PeeContributionRepository peeContributionRepository;

        @Transactional
        public ContractOutDTO createContract(ContractInDTO contractdDto) {

                // Create Contract
                Contract contract = new Contract();
                contract.setClosingMonth(contractdDto.closingMonth());
                contract.setEligibility(contractdDto.eligibility());
                contract = contractRepository.save(contract);

                // Create Company
                Company company = new Company();
                company.setSiret(contractdDto.company().getSiret());
                company.setSiren(contractdDto.company().getSiren());
                company.setBusinessActivity(contractdDto.company().getBusinessActivity());
                company.setBusinessAddress(contractdDto.company().getBusinessAddress());
                company.setLegalForm(contractdDto.company().getLegalForm());
                company.setCompanyName(contractdDto.company().getCompanyName());
                company.setTotalWages(contractdDto.company().getTotalWages());
                company.setWorkforce(contractdDto.company().getWorkforce());
                company.setContractId(contract);
                companyRepository.save(company);
                contract.setCompany(company);

                // Create CompanySignatory
                CompanySignatory companySignatory = new CompanySignatory();
                companySignatory.setCountryOfResidence(contractdDto.companySignatory().getCountryOfResidence());
                companySignatory.setCountryOfBirth(contractdDto.companySignatory().getCountryOfBirth());
                companySignatory.setDateOfBirth(LocalDate.parse(contractdDto.companySignatory().getDateOfBirth()));
                companySignatory.setEmail(contractdDto.companySignatory().getEmail());
                companySignatory.setExecutive(contractdDto.companySignatory().getExecutive());
                companySignatory.setJobTitle(contractdDto.companySignatory().getJobTitle());
                companySignatory.setSocialSecurityNumber(contractdDto.companySignatory().getSocialSecurityNumber());
                companySignatory.setLastName(contractdDto.companySignatory().getLastName());
                companySignatory.setFirstName(contractdDto.companySignatory().getFirstName());
                companySignatory.setContractId(contract);
                companySignatoryRepository.save(companySignatory);
                contract.setCompanySignatory(companySignatory);

                // Create PeeContribution if it exists
                if (contractdDto.peeContribution().isPresent()) {
                        var peeContributionBody = contractdDto.peeContribution().get();
                        PeeContribution peeContribution = new PeeContribution();
                        peeContribution.setRateSimpleContribution(
                                        peeContributionBody.getRateSimpleContribution());
                        peeContribution.setCeilingSimpleContribution(
                                        peeContributionBody.getCeilingSimpleContribution());
                        peeContribution.setRateSeniorityContribution(
                                        peeContributionBody.getRateSeniorityContribution());
                        peeContribution.setCeilingSeniorityContributionLessYear(
                                        peeContributionBody.getCeilingSeniorityContributionLessYear());
                        peeContribution.setCeilingSeniorityContributionBetween1And3(
                                        peeContributionBody.getCeilingSeniorityContributionBetween1And3());
                        peeContribution.setCeilingSeniorityContributionBetween3And5(
                                        peeContributionBody.getCeilingSeniorityContributionBetween3And5());
                        peeContribution.setCeilingSeniorityContributionGreater5(
                                        peeContributionBody.getCeilingSeniorityContributionGreater5());
                        peeContribution.setCeilingIntervalContributionFirst(
                                        peeContributionBody.getCeilingIntervalContributionFirst());
                        peeContribution.setRateIntervalContributionFirst(
                                        peeContributionBody.getRateIntervalContributionFirst());
                        peeContribution.setIntervalContributionFirst(
                                        peeContributionBody.getIntervalContributionFirst());
                        peeContribution.setCeilingIntervalContributionSecond(
                                        peeContributionBody.getCeilingIntervalContributionSecond());
                        peeContribution
                                        .setRateIntervalContributionSecond(peeContributionBody
                                                        .getRateIntervalContributionSecond());
                        peeContribution.setIntervalContributionSecond(
                                        peeContributionBody.getIntervalContributionSecond());
                        peeContribution.setCeilingIntervalContributionThird(
                                        peeContributionBody.getCeilingIntervalContributionThird());
                        peeContribution
                                        .setRateIntervalContributionThird(
                                                        peeContributionBody
                                                                        .getRateIntervalContributionThird());
                        peeContribution.setIntervalContributionThird(
                                        peeContributionBody.getIntervalContributionThird());
                        peeContribution.setPeeInterestAccepted(peeContributionBody.isPeeInterestAccepted());
                        peeContribution
                                        .setPeeVoluntaryDepositAccepted(
                                                        peeContributionBody.isPeeVoluntaryDepositAccepted());
                        peeContribution.setPeeProfitSharingAccepted(
                                        peeContributionBody.isPeeProfitSharingAccepted());
                        peeContribution.setContract(contract);

                        peeContributionRepository.save(peeContribution);
                        contract.setPeeContribution(peeContribution);

                }

                // Create PerecoContribution if it exists
                if (contractdDto.percoContribution().isPresent()) {
                        var peeContributionBody = contractdDto.percoContribution().get();
                        PercoContribution percoContribution = new PercoContribution();
                        percoContribution.setRateSimpleContribution(
                                        peeContributionBody.getRateSimpleContribution());
                        percoContribution
                                        .setCeilingSimpleContribution(
                                                        peeContributionBody
                                                                        .getCeilingSimpleContribution());
                        percoContribution
                                        .setRateSeniorityContribution(
                                                        peeContributionBody
                                                                        .getRateSeniorityContribution());
                        percoContribution.setCeilingSeniorityContributionLessYear(
                                        peeContributionBody.getCeilingSeniorityContributionLessYear());
                        percoContribution.setCeilingSeniorityContributionBetween1And3(
                                        peeContributionBody
                                                        .getCeilingSeniorityContributionBetween1And3());
                        percoContribution.setCeilingSeniorityContributionBetween3And5(
                                        peeContributionBody
                                                        .getCeilingSeniorityContributionBetween3And5());
                        percoContribution.setCeilingSeniorityContributionGreater5(
                                        peeContributionBody.getCeilingSeniorityContributionGreater5());
                        percoContribution.setCeilingIntervalContributionFirst(
                                        peeContributionBody.getCeilingIntervalContributionFirst());
                        percoContribution.setRateIntervalContributionFirst(
                                        peeContributionBody.getRateIntervalContributionFirst());
                        percoContribution
                                        .setIntervalContributionFirst(
                                                        peeContributionBody
                                                                        .getIntervalContributionFirst());
                        percoContribution.setCeilingIntervalContributionSecond(
                                        peeContributionBody.getCeilingIntervalContributionSecond());
                        percoContribution.setRateIntervalContributionSecond(
                                        peeContributionBody.getRateIntervalContributionSecond());
                        percoContribution
                                        .setIntervalContributionSecond(
                                                        peeContributionBody
                                                                        .getIntervalContributionSecond());
                        percoContribution.setCeilingIntervalContributionThird(
                                        peeContributionBody.getCeilingIntervalContributionThird());
                        percoContribution.setRateIntervalContributionThird(
                                        peeContributionBody.getRateIntervalContributionThird());
                        percoContribution
                                        .setIntervalContributionThird(
                                                        peeContributionBody
                                                                        .getIntervalContributionThird());
                        percoContribution.setPercoInterestAccepted(
                                        peeContributionBody.isPercoInterestAccepted());
                        percoContribution.setPercoVoluntaryDepositAccepted(
                                        peeContributionBody.isPercoVoluntaryDepositAccepted());
                        percoContribution
                                        .setPercoProfitSharingAccepted(peeContributionBody
                                                        .isPercoProfitSharingAccepted());
                        percoContribution.setPercoTimeSavingAccountAccepted(
                                        peeContributionBody.isPercoTimeSavingAccountAccepted());
                        percoContribution.setContract(contract);

                        perecoContributionRepository.save(percoContribution);
                        contract.setPercoContribution(percoContribution);
                }

                return mapContractToContractOutDto(contract);
        }

        public List<ContractOutDTO> listAllContracts() {
                List<Contract> contracts = this.contractRepository.findAll();

                return contracts.stream().map((c) -> mapContractToContractOutDto(c)).collect(Collectors.toList());
        }

        private ContractOutDTO mapContractToContractOutDto(Contract contract) {
                ContractOutDTOBuilder contractOutDtoBuilder = ContractOutDTO.builder()
                                .closingMonth(contract.getClosingMonth())
                                .eligibility(contract.getEligibility())
                                .company(CompanyDTO.builder()
                                                .businessActivity(contract.getCompany().getBusinessActivity())
                                                .businessAddress(contract.getCompany().getBusinessAddress())
                                                .siret(contract.getCompany().getSiret())
                                                .siren(contract.getCompany().getSiren())
                                                .companyName(contract.getCompany().getCompanyName())
                                                .legalForm(contract.getCompany().getLegalForm())
                                                .workforce(contract.getCompany().getWorkforce())
                                                .totalWages(contract.getCompany().getTotalWages())
                                                .build())
                                .companySignatory(CompanySignatoryDTO.builder()
                                                .lastName(contract.getCompanySignatory().getLastName())
                                                .firstName(contract.getCompanySignatory().getFirstName())
                                                .email(contract.getCompanySignatory().getEmail())
                                                .jobTitle(contract.getCompanySignatory().getJobTitle())
                                                .dateOfBirth(contract.getCompanySignatory().getDateOfBirth().toString())
                                                .socialSecurityNumber(contract.getCompanySignatory()
                                                                .getSocialSecurityNumber())
                                                .countryOfBirth(contract.getCompanySignatory().getCountryOfBirth())
                                                .countryOfResidence(
                                                                contract.getCompanySignatory().getCountryOfResidence())
                                                .executive(contract.getCompanySignatory().getExecutive())
                                                .build());

                if (contract.getPeeContribution() != null) {
                        contractOutDtoBuilder
                                        .peeContribution(PeeContributionDTO.builder()
                                                        .rateSimpleContribution(contract.getPeeContribution()
                                                                        .getRateSimpleContribution())
                                                        .ceilingSimpleContribution(contract.getPeeContribution()
                                                                        .getCeilingSimpleContribution())
                                                        .rateSeniorityContribution(contract.getPeeContribution()
                                                                        .getRateSeniorityContribution())
                                                        .ceilingSeniorityContributionLessYear(contract
                                                                        .getPeeContribution()
                                                                        .getCeilingSeniorityContributionLessYear())
                                                        .ceilingSeniorityContributionBetween1And3(contract
                                                                        .getPeeContribution()
                                                                        .getCeilingSeniorityContributionBetween1And3())
                                                        .ceilingSeniorityContributionBetween3And5(contract
                                                                        .getPeeContribution()
                                                                        .getCeilingSeniorityContributionBetween3And5())
                                                        .ceilingSeniorityContributionGreater5(contract
                                                                        .getPeeContribution()
                                                                        .getCeilingSeniorityContributionGreater5())
                                                        .ceilingIntervalContributionFirst(contract.getPeeContribution()
                                                                        .getCeilingIntervalContributionFirst())
                                                        .rateIntervalContributionFirst(contract.getPeeContribution()
                                                                        .getRateIntervalContributionFirst())
                                                        .intervalContributionFirst(contract.getPeeContribution()
                                                                        .getIntervalContributionFirst())
                                                        .ceilingIntervalContributionSecond(contract.getPeeContribution()
                                                                        .getCeilingIntervalContributionSecond())
                                                        .rateIntervalContributionSecond(contract.getPeeContribution()
                                                                        .getRateIntervalContributionSecond())
                                                        .intervalContributionSecond(contract.getPeeContribution()
                                                                        .getIntervalContributionSecond())
                                                        .ceilingIntervalContributionThird(contract.getPeeContribution()
                                                                        .getCeilingIntervalContributionThird())
                                                        .rateIntervalContributionThird(contract.getPeeContribution()
                                                                        .getRateIntervalContributionThird())
                                                        .intervalContributionThird(contract.getPeeContribution()
                                                                        .getIntervalContributionThird())
                                                        .peeInterestAccepted(
                                                                        contract.getPeeContribution()
                                                                                        .isPeeInterestAccepted())
                                                        .peeVoluntaryDepositAccepted(contract.getPeeContribution()
                                                                        .isPeeVoluntaryDepositAccepted())
                                                        .peeProfitSharingAccepted(contract.getPeeContribution()
                                                                        .isPeeProfitSharingAccepted())
                                                        .build());
                }

                if (contract.getPercoContribution() != null) {
                        contractOutDtoBuilder
                                        .percoContribution(PercoContributionDTO.builder()
                                                        .rateSimpleContribution(contract.getPercoContribution()
                                                                        .getRateSimpleContribution())
                                                        .ceilingSimpleContribution(contract.getPercoContribution()
                                                                        .getCeilingSimpleContribution())
                                                        .rateSeniorityContribution(contract.getPercoContribution()
                                                                        .getRateSeniorityContribution())
                                                        .ceilingSeniorityContributionLessYear(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingSeniorityContributionLessYear())
                                                        .ceilingSeniorityContributionBetween1And3(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingSeniorityContributionBetween1And3())
                                                        .ceilingSeniorityContributionBetween3And5(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingSeniorityContributionBetween3And5())
                                                        .ceilingSeniorityContributionGreater5(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingSeniorityContributionGreater5())
                                                        .ceilingIntervalContributionFirst(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingIntervalContributionFirst())
                                                        .rateIntervalContributionFirst(contract.getPercoContribution()
                                                                        .getRateIntervalContributionFirst())
                                                        .intervalContributionFirst(contract.getPercoContribution()
                                                                        .getIntervalContributionFirst())
                                                        .ceilingIntervalContributionSecond(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingIntervalContributionSecond())
                                                        .rateIntervalContributionSecond(contract.getPercoContribution()
                                                                        .getRateIntervalContributionSecond())
                                                        .intervalContributionSecond(contract.getPercoContribution()
                                                                        .getIntervalContributionSecond())
                                                        .ceilingIntervalContributionThird(contract
                                                                        .getPercoContribution()
                                                                        .getCeilingIntervalContributionThird())
                                                        .rateIntervalContributionThird(contract.getPercoContribution()
                                                                        .getRateIntervalContributionThird())
                                                        .intervalContributionThird(contract.getPercoContribution()
                                                                        .getIntervalContributionThird())
                                                        .percoInterestAccepted(contract.getPercoContribution()
                                                                        .getPercoInterestAccepted())
                                                        .percoVoluntaryDepositAccepted(contract.getPercoContribution()
                                                                        .getPercoVoluntaryDepositAccepted())
                                                        .percoProfitSharingAccepted(contract.getPercoContribution()
                                                                        .getPercoProfitSharingAccepted())
                                                        .percoTimeSavingAccountAccepted(contract.getPercoContribution()
                                                                        .getPercoTimeSavingAccountAccepted())
                                                        .build());
                }
                // Include an empty list of accounts
                contractOutDtoBuilder.accounts(List.of());
                return contractOutDtoBuilder.build();
        }
}
