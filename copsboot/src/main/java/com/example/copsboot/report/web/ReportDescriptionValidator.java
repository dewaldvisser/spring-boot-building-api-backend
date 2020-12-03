package com.example.copsboot.report.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReportDescriptionValidator implements ConstraintValidator<ValidReportDescription, String> { //<1>

    @Override
    public void initialize(ValidReportDescription constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean result = true;
        if (!value.toLowerCase().contains("suspect")) {
            result = false;
        }
        return result;
    }
}
