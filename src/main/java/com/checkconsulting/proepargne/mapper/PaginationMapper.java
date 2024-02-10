package com.checkconsulting.proepargne.mapper;


import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.model.Pagination;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PaginationMapper {

    Pagination mapToPagination(Page<TransactionOutDto> paginatedTransaction);

}
