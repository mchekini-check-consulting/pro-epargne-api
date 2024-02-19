package com.checkconsulting.proepargne.handlers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTemplate {

    private Object data;
    private String errorMessage;
    private Map<String, String> validations;
}