package com.checkconsulting.proepargne.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.checkconsulting.proepargne.dto.contract.CompanyDTO;
import com.checkconsulting.proepargne.dto.contract.CompanySignatoryDTO;
import com.checkconsulting.proepargne.dto.contract.ContractInDTO;
import com.checkconsulting.proepargne.dto.contract.ContractOutDTO;
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
                if (contractdDto.peeContribution() != null) {
                        PeeContribution peeContribution = new PeeContribution();
                        peeContribution.setRateSimpleContribution(
                                        contractdDto.peeContribution().getRateSimpleContribution());
                        peeContribution.setCeilingSimpleContribution(
                                        contractdDto.peeContribution().getCeilingSimpleContribution());
                        peeContribution.setRateSeniorityContribution(
                                        contractdDto.peeContribution().getRateSeniorityContribution());
                        peeContribution.setCeilingSeniorityContributionLessYear(
                                        contractdDto.peeContribution().getCeilingSeniorityContributionLessYear());
                        peeContribution.setCeilingSeniorityContributionBetween1And3(
                                        contractdDto.peeContribution().getCeilingSeniorityContributionBetween1And3());
                        peeContribution.setCeilingSeniorityContributionBetween3And5(
                                        contractdDto.peeContribution().getCeilingSeniorityContributionBetween3And5());
                        peeContribution.setCeilingSeniorityContributionGreater5(
                                        contractdDto.peeContribution().getCeilingSeniorityContributionGreater5());
                        peeContribution.setCeilingIntervalContributionFirst(
                                        contractdDto.peeContribution().getCeilingIntervalContributionFirst());
                        peeContribution.setRateIntervalContributionFirst(
                                        contractdDto.peeContribution().getRateIntervalContributionFirst());
                        peeContribution.setIntervalContributionFirst(
                                        contractdDto.peeContribution().getIntervalContributionFirst());
                        peeContribution.setCeilingIntervalContributionSecond(
                                        contractdDto.peeContribution().getCeilingIntervalContributionSecond());
                        peeContribution
                                        .setRateIntervalContributionSecond(contractdDto.peeContribution()
                                                        .getRateIntervalContributionSecond());
                        peeContribution.setIntervalContributionSecond(
                                        contractdDto.peeContribution().getIntervalContributionSecond());
                        peeContribution.setCeilingIntervalContributionThird(
                                        contractdDto.peeContribution().getCeilingIntervalContributionThird());
                        peeContribution
                                        .setRateIntervalContributionThird(
                                                        contractdDto.peeContribution()
                                                                        .getRateIntervalContributionThird());
                        peeContribution.setIntervalContributionThird(
                                        contractdDto.peeContribution().getIntervalContributionThird());
                        peeContribution.setPeeInterestAccepted(contractdDto.peeContribution().isPeeInterestAccepted());
                        peeContribution
                                        .setPeeVoluntaryDepositAccepted(
                                                        contractdDto.peeContribution().isPeeVoluntaryDepositAccepted());
                        peeContribution.setPeeProfitSharingAccepted(
                                        contractdDto.peeContribution().isPeeProfitSharingAccepte());
                        peeContribution.setContract(contract);

                        peeContributionRepository.save(peeContribution);
                        contract.setPeeContribution(peeContribution);

                }

                // Create PerecoContribution if it exists
                if (contractdDto.percoContribution() != null) {
                        PercoContribution pereContribution = new PercoContribution();
                        pereContribution.setRateSimpleContribution(
                                        contractdDto.percoContribution().getRateSimpleContribution());
                        pereContribution
                                        .setCeilingSimpleContribution(
                                                        contractdDto.percoContribution()
                                                                        .getCeilingSimpleContribution());
                        pereContribution
                                        .setRateSeniorityContribution(
                                                        contractdDto.percoContribution()
                                                                        .getRateSeniorityContribution());
                        pereContribution.setCeilingSeniorityContributionLessYear(
                                        contractdDto.percoContribution().getCeilingSeniorityContributionLessYear());
                        pereContribution.setCeilingSeniorityContributionBetween1And3(
                                        contractdDto.percoContribution()
                                                        .getCeilingSeniorityContributionBetween1And3());
                        pereContribution.setCeilingSeniorityContributionBetween3And5(
                                        contractdDto.percoContribution()
                                                        .getCeilingSeniorityContributionBetween3And5());
                        pereContribution.setCeilingSeniorityContributionGreater5(
                                        contractdDto.percoContribution().getCeilingSeniorityContributionGreater5());
                        pereContribution.setCeilingIntervalContributionFirst(
                                        contractdDto.percoContribution().getCeilingIntervalContributionFirst());
                        pereContribution.setRateIntervalContributionFirst(
                                        contractdDto.percoContribution().getRateIntervalContributionFirst());
                        pereContribution
                                        .setIntervalContributionFirst(
                                                        contractdDto.percoContribution()
                                                                        .getIntervalContributionFirst());
                        pereContribution.setCeilingIntervalContributionSecond(
                                        contractdDto.percoContribution().getCeilingIntervalContributionSecond());
                        pereContribution.setRateIntervalContributionSecond(
                                        contractdDto.percoContribution().getRateIntervalContributionSecond());
                        pereContribution
                                        .setIntervalContributionSecond(
                                                        contractdDto.percoContribution()
                                                                        .getIntervalContributionSecond());
                        pereContribution.setCeilingIntervalContributionThird(
                                        contractdDto.percoContribution().getCeilingIntervalContributionThird());
                        pereContribution.setRateIntervalContributionThird(
                                        contractdDto.percoContribution().getRateIntervalContributionThird());
                        pereContribution
                                        .setIntervalContributionThird(
                                                        contractdDto.percoContribution()
                                                                        .getIntervalContributionThird());
                        pereContribution.setPercoInterestAccepted(
                                        contractdDto.percoContribution().isPercoInterestAccepted());
                        pereContribution.setPercoVoluntaryDepositAccepted(
                                        contractdDto.percoContribution().isPercoVoluntaryDepositAccepted());
                        pereContribution
                                        .setPercoProfitSharingAccepted(contractdDto.percoContribution()
                                                        .isPercoProfitSharingAccepted());
                        pereContribution.setPercoTimeSavingAccountAccepted(
                                        contractdDto.percoContribution().isPercoTimeSavingAccountAccepte());
                        pereContribution.setContract(contract);

                        perecoContributionRepository.save(pereContribution);
                        contract.setPerecoContribution(pereContribution);
                }

                ContractOutDTO contractOutDto = ContractOutDTO.builder()
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
                                                .build())
                                .peeContribution(PeeContributionDTO.builder()
                                                .rateSimpleContribution(contract.getPeeContribution()
                                                                .getRateSimpleContribution())
                                                .ceilingSimpleContribution(contract.getPeeContribution()
                                                                .getCeilingSimpleContribution())
                                                .rateSeniorityContribution(contract.getPeeContribution()
                                                                .getRateSeniorityContribution())
                                                .ceilingSeniorityContributionLessYear(contract.getPeeContribution()
                                                                .getCeilingSeniorityContributionLessYear())
                                                .ceilingSeniorityContributionBetween1And3(contract.getPeeContribution()
                                                                .getCeilingSeniorityContributionBetween1And3())
                                                .ceilingSeniorityContributionBetween3And5(contract.getPeeContribution()
                                                                .getCeilingSeniorityContributionBetween3And5())
                                                .ceilingSeniorityContributionGreater5(contract.getPeeContribution()
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
                                                                contract.getPeeContribution().isPeeInterestAccepted())
                                                .peeVoluntaryDepositAccepted(contract.getPeeContribution()
                                                                .isPeeVoluntaryDepositAccepted())
                                                .peeProfitSharingAccepte(contract.getPeeContribution()
                                                                .isPeeProfitSharingAccepted())
                                                .build())
                                .percoContribution(PercoContributionDTO.builder()
                                                .rateSimpleContribution(contract.getPerecoContribution()
                                                                .getRateSimpleContribution())
                                                .ceilingSimpleContribution(contract.getPerecoContribution()
                                                                .getCeilingSimpleContribution())
                                                .rateSeniorityContribution(contract.getPerecoContribution()
                                                                .getRateSeniorityContribution())
                                                .ceilingSeniorityContributionLessYear(contract.getPerecoContribution()
                                                                .getCeilingSeniorityContributionLessYear())
                                                .ceilingSeniorityContributionBetween1And3(contract
                                                                .getPerecoContribution()
                                                                .getCeilingSeniorityContributionBetween1And3())
                                                .ceilingSeniorityContributionBetween3And5(contract
                                                                .getPerecoContribution()
                                                                .getCeilingSeniorityContributionBetween3And5())
                                                .ceilingSeniorityContributionGreater5(contract.getPerecoContribution()
                                                                .getCeilingSeniorityContributionGreater5())
                                                .ceilingIntervalContributionFirst(contract.getPerecoContribution()
                                                                .getCeilingIntervalContributionFirst())
                                                .rateIntervalContributionFirst(contract.getPerecoContribution()
                                                                .getRateIntervalContributionFirst())
                                                .intervalContributionFirst(contract.getPerecoContribution()
                                                                .getIntervalContributionFirst())
                                                .ceilingIntervalContributionSecond(contract.getPerecoContribution()
                                                                .getCeilingIntervalContributionSecond())
                                                .rateIntervalContributionSecond(contract.getPerecoContribution()
                                                                .getRateIntervalContributionSecond())
                                                .intervalContributionSecond(contract.getPerecoContribution()
                                                                .getIntervalContributionSecond())
                                                .ceilingIntervalContributionThird(contract.getPerecoContribution()
                                                                .getCeilingIntervalContributionThird())
                                                .rateIntervalContributionThird(contract.getPerecoContribution()
                                                                .getRateIntervalContributionThird())
                                                .intervalContributionThird(contract.getPerecoContribution()
                                                                .getIntervalContributionThird())
                                                .percoInterestAccepted(contract.getPerecoContribution()
                                                                .getPercoInterestAccepted())
                                                .percoVoluntaryDepositAccepted(contract.getPerecoContribution()
                                                                .getPercoVoluntaryDepositAccepted())
                                                .percoProfitSharingAccepted(contract.getPerecoContribution()
                                                                .getPercoProfitSharingAccepted())
                                                .percoTimeSavingAccountAccepte(contract.getPerecoContribution()
                                                                .getPercoTimeSavingAccountAccepted())
                                                .build())
                                .accounts(List.of())
                                .build();
                // contractOutDto.get
                // create the out dto
                return contractOutDto;
        }
}
