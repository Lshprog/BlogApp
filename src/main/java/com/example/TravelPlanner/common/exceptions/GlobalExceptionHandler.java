package com.example.TravelPlanner.common.exceptions;

import com.example.TravelPlanner.common.exceptions.custom.AlreadyVotedException;
import com.example.TravelPlanner.common.exceptions.custom.CustomAuthException;
import com.example.TravelPlanner.common.exceptions.custom.EntityNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.UserNotPartOfTravelPlanException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<Object> handleUnauthorized(CustomAuthException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AlreadyVotedException.class)
    public ResponseEntity<Object> handleAlreadyVoted(AlreadyVotedException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Other exception handlers...
}
