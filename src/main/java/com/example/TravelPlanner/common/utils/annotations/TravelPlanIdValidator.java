package com.example.TravelPlanner.common.utils.annotations;

import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
public class TravelPlanIdValidator implements ConstraintValidator<ValidTravelPlanId, Long> {

    private final TravelPlanRepository travelPlanRepository;

    @Override
    public void initialize(ValidTravelPlanId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long travelPlanId, ConstraintValidatorContext context) {
        if (travelPlanId == null || !travelPlanRepository.existsById(travelPlanId)) {
            return false;
        }
        return true;
    }
}
