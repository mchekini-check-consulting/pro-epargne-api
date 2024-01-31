package com.checkconsulting.proepargne.services;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.checkconsulting.proepargne.dto.contract.ContractInDTO;
import com.checkconsulting.proepargne.model.Company;
import com.checkconsulting.proepargne.model.CompanySignatory;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.PeeContribution;
import com.checkconsulting.proepargne.model.PerecoContribution;
import com.checkconsulting.proepargne.repository.CompanyRepository;
import com.checkconsulting.proepargne.repository.CompanySignatoryRepository;
import com.checkconsulting.proepargne.repository.ContractRepository;
import com.checkconsulting.proepargne.repository.PeeContributionRepository;
import com.checkconsulting.proepargne.repository.PerecoContributionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final CompanyRepository companyRepository;
    private final ContractRepository contractRepository;
    private final CompanySignatoryRepository companySignatoryRepository;
    private final PerecoContributionRepository perecoContributionRepository;
    private final PeeContributionRepository peeContributionRepository;

    @Transactional
    public Contract createContract(@RequestBody ContractInDTO contractdDto) {

        // Create Contract
        Contract contract = new Contract();
        contract.setClosingMonth(contractdDto.closingMonth());
        contract.setEligibility(contractdDto.eligibility());
        contract = contractRepository.save(contract);

        // Create Company
        Company company = new Company();
        company.setSiret(contractdDto.company().siret());
        company.setBusinessActivity(contractdDto.company().businessActivity());
        company.setBusinessAddress(contractdDto.company().businessAddress());
        company.setLegalForm(contractdDto.company().legalForm());
        company.setCompanyName(contractdDto.company().companyName());
        company.setTotalWages(contractdDto.company().totalWages());
        company.setWorkforce(contractdDto.company().workforce());
        company.setContractId(contract);
        companyRepository.save(company);
        contract.setCompany(company);

        // Create CompanySignatory
        CompanySignatory companySignatory = new CompanySignatory();
        companySignatory.setCountryOfResidence(contractdDto.companySignatory().countryOfResidence());
        companySignatory.setCountryOfBirth(contractdDto.companySignatory().countryOfBirth());
        companySignatory.setDateOfBirth(LocalDate.parse(contractdDto.companySignatory().dateOfBirth()));// double check
        companySignatory.setEmail(contractdDto.companySignatory().email());
        companySignatory.setExecutive(contractdDto.companySignatory().executive());
        companySignatory.setJobTitle(contractdDto.companySignatory().jobTitle());
        companySignatory.setSocialSecurityNumber(contractdDto.companySignatory().socialSecurityNumber());
        companySignatory.setLastName(contractdDto.companySignatory().lastName());
        companySignatory.setFirstName(contractdDto.companySignatory().firstName());
        companySignatory.setContractId(contract);
        companySignatoryRepository.save(companySignatory);
        contract.setCompanySignatory(companySignatory);

        // Create PeeContribution if it exists
        if (contractdDto.peeContribution() != null) {
            PeeContribution peeContribution = new PeeContribution();
            peeContribution.setRateSimpleContribution(contractdDto.peeContribution().rateSimpleContribution());
            peeContribution.setCeilingSimpleContribution(contractdDto.peeContribution().ceilingSimpleContribution());
            peeContribution.setRateSeniorityContribution(contractdDto.peeContribution().rateSeniorityContribution());
            peeContribution.setCeilingSeniorityContributionLessYear(
                    contractdDto.peeContribution().ceilingSeniorityContributionLessYear());
            peeContribution.setCeilingSeniorityContributionBetween1And3(
                    contractdDto.peeContribution().ceilingSeniorityContributionBetween1And3());
            peeContribution.setCeilingSeniorityContributionBetween3And5(
                    contractdDto.peeContribution().ceilingSeniorityContributionBetween3And5());
            peeContribution.setCeilingSeniorityContributionGreater5(
                    contractdDto.peeContribution().ceilingSeniorityContributionGreater5());
            peeContribution.setCeilingIntervalContributionFirst(
                    contractdDto.peeContribution().ceilingIntervalContributionFirst());
            peeContribution
                    .setRateIntervalContributionFirst(contractdDto.peeContribution().rateIntervalContributionFirst());
            peeContribution.setIntervalContributionFirst(contractdDto.peeContribution().intervalContributionFirst());
            peeContribution.setCeilingIntervalContributionSecond(
                    contractdDto.peeContribution().ceilingIntervalContributionSecond());
            peeContribution
                    .setRateIntervalContributionSecond(contractdDto.peeContribution().rateIntervalContributionSecond());
            peeContribution.setIntervalContributionSecond(contractdDto.peeContribution().intervalContributionSecond());
            peeContribution.setCeilingIntervalContributionThird(
                    contractdDto.peeContribution().ceilingIntervalContributionThird());
            peeContribution
                    .setRateIntervalContributionThird(contractdDto.peeContribution().rateIntervalContributionThird());
            peeContribution.setIntervalContributionThird(contractdDto.peeContribution().intervalContributionThird());
            peeContribution.setPeeInterestAccepted(contractdDto.peeContribution().peeInterestAccepted());
            peeContribution
                    .setPeeVoluntaryDepositAccepted(contractdDto.peeContribution().peeVoluntaryDepositAccepted());
            peeContribution.setPeeProfitSharingAccepted(contractdDto.peeContribution().peeProfitSharingAccepted());
            peeContribution.setContract(contract);

            peeContributionRepository.save(peeContribution);
            contract.setPeeContribution(peeContribution);

        }

        // Create PerecoContribution if it exists
        if (contractdDto.perecoContribution() != null) {
            PerecoContribution pereContribution = new PerecoContribution();
            pereContribution.setRateSimpleContribution(contractdDto.perecoContribution().rateSimpleContribution());
            pereContribution
                    .setCeilingSimpleContribution(contractdDto.perecoContribution().ceilingSimpleContribution());
            pereContribution
                    .setRateSeniorityContribution(contractdDto.perecoContribution().rateSeniorityContribution());
            pereContribution.setCeilingSeniorityContributionLessYear(
                    contractdDto.perecoContribution().ceilingSeniorityContributionLessYear());
            pereContribution.setCeilingSeniorityContributionBetween1And3(
                    contractdDto.perecoContribution().ceilingSeniorityContributionBetween1And3());
            pereContribution.setCeilingSeniorityContributionBetween3And5(
                    contractdDto.perecoContribution().ceilingSeniorityContributionBetween3And5());
            pereContribution.setCeilingSeniorityContributionGreater5(
                    contractdDto.perecoContribution().ceilingSeniorityContributionGreater5());
            pereContribution.setCeilingIntervalContributionFirst(
                    contractdDto.perecoContribution().ceilingIntervalContributionFirst());
            pereContribution.setRateIntervalContributionFirst(
                    contractdDto.perecoContribution().rateIntervalContributionFirst());
            pereContribution
                    .setIntervalContributionFirst(contractdDto.perecoContribution().intervalContributionFirst());
            pereContribution.setCeilingIntervalContributionSecond(
                    contractdDto.perecoContribution().ceilingIntervalContributionSecond());
            pereContribution.setRateIntervalContributionSecond(
                    contractdDto.perecoContribution().rateIntervalContributionSecond());
            pereContribution
                    .setIntervalContributionSecond(contractdDto.perecoContribution().intervalContributionSecond());
            pereContribution.setCeilingIntervalContributionThird(
                    contractdDto.perecoContribution().ceilingIntervalContributionThird());
            pereContribution.setRateIntervalContributionThird(
                    contractdDto.perecoContribution().rateIntervalContributionThird());
            pereContribution
                    .setIntervalContributionThird(contractdDto.perecoContribution().intervalContributionThird());
            pereContribution.setPerecoInterestAccepted(contractdDto.perecoContribution().perecoInterestAccepted());
            pereContribution.setPerecoVoluntaryDepositAccepted(
                    contractdDto.perecoContribution().perecoVoluntaryDepositAccepted());
            pereContribution
                    .setPerecoProfitSharingAccepted(contractdDto.perecoContribution().perecoProfitSharingAccepted());
            pereContribution.setPerecoTimeSavingAccountAccepted(
                    contractdDto.perecoContribution().perecoTimeSavingAccountAccepted());
            pereContribution.setContract(contract);

            perecoContributionRepository.save(pereContribution);
            contract.setPerecoContribution(pereContribution);
        }


        //create the out dto
        return contract;
    }
}
