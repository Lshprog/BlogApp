package com.example.TravelPlanner.common.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VotingIdValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidVotingId {
    String message() default "Invalid Voting ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
