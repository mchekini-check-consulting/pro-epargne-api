package com.checkconsulting.proepargne.dto.requestWithdrawal;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestWithdrawalDto {

    private String typeAccount;
    private String reasonUnblocking;
    private String rib;
    private Float amount;
}
