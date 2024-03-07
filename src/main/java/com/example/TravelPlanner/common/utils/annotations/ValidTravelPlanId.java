package com.example.TravelPlanner.common.utils.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TravelPlanIdValidator.class)
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTravelPlanId {

    String message() default "Invalid Travel Plan ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
