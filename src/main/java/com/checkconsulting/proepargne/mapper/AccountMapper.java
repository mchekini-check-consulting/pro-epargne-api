package com.checkconsulting.proepargne.mapper;

import com.checkconsulting.proepargne.dto.account.AccountOutDto;
import com.checkconsulting.proepargne.model.Account;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountOutDto mapToAccountOutDto(Account account);
}
