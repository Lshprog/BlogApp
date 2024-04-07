package com.example.TravelPlanner.common.exceptions.custom;


public class NoPermissionException extends CustomAuthException {

    public NoPermissionException(String message) {
        super(message);
    }
    public NoPermissionException() {
        super("No permission to perform this!");
    }


}
