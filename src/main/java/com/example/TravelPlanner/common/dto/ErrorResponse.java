package com.example.TravelPlanner.common.dto;

public class ErrorResponse {

    public ErrorResponse(RuntimeException ex) {
        this.error = ex.getMessage();
    }

    private String error;



}
