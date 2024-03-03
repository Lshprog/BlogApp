package com.example.TravelPlanner.common.exceptions.custom;

public class NotOwnerException extends CustomAuthException {
    public NotOwnerException() {
        super("Not owner/creator");
    }
}
