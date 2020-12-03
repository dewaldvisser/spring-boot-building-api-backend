package com.example.copsboot.report.web;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CreateReportParametersValidator implements ConstraintValidator<ValidCreateReportParameters, CreateReportParameters> {

    @Override
    public void initialize(ValidCreateReportParameters constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateReportParameters value, ConstraintValidatorContext context) {
        boolean result = true;
        if (value.isTrafficIncident() && value.getNumberOfInvolvedCars() <= 0) {
            result = false;
        }
        return result;
    }
}
