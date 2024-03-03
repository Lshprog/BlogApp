package com.example.TravelPlanner.common.exceptions.custom;


public class UserNotPartOfTravelPlanException extends CustomAuthException {
    public UserNotPartOfTravelPlanException(){
        super("User is not part of the travel plan");
    }
}
