package com.checkconsulting.proepargne.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.checkconsulting.proepargne.dto.account.AccountOutDto;
import com.checkconsulting.proepargne.model.Account;

@Mapper(componentModel = "spring")
@Component
public interface AccoutMapper {
    AccountOutDto mapAccountToAccountOutDto(Account source);
}
