package com.example.copsboot.report.web;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ReportDescriptionValidator.class})
public @interface ValidReportDescription {
    String message() default "Invalid report description";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}