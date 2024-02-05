package com.checkconsulting.proepargne.validators;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomEnumValidator implements ConstraintValidator<EnumValidator, String> {
    private List<String> acceptedValues;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants()).map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return acceptedValues.contains(value.toString());
    }

}
