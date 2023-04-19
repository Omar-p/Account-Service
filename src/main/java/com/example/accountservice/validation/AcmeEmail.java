package com.example.accountservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AcmeEmailValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Email
public @interface AcmeEmail {
    String message() default "Email must belong to acme.com domain";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
