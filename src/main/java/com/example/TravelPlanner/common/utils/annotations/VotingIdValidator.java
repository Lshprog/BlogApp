package com.example.TravelPlanner.common.utils.annotations;

import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.VotingNotFoundException;
import com.example.TravelPlanner.travelplanning.repositories.VotingRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VotingIdValidator implements ConstraintValidator<ValidVotingId, Long> {

    private final VotingRepository votingRepository;

    @Override
    public void initialize(ValidVotingId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long votingId, ConstraintValidatorContext constraintValidatorContext) {
        if (votingId == null || !votingRepository.existsById(votingId)) {
            throw new VotingNotFoundException(votingId);
        }
        return true;
    }
}
