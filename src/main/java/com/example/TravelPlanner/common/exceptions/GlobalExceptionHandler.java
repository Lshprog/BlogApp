package com.example.TravelPlanner.common.exceptions;

import com.example.TravelPlanner.common.exceptions.custom.AlreadyVotedException;
import com.example.TravelPlanner.common.exceptions.custom.BadRequest;
import com.example.TravelPlanner.common.exceptions.custom.CustomAuthException;
import com.example.TravelPlanner.common.exceptions.custom.OverlappingEventsException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EntityNotFoundException;
import io.jsonwebtoken.JwtException;
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
        return buildSimpleResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomAuthException.class)
    public ResponseEntity<Object> handleUnauthorized(CustomAuthException ex) {
        return buildSimpleResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AlreadyVotedException.class, OverlappingEventsException.class, JwtException.class, BadRequest.class})
    public ResponseEntity<Object> handleBadRequest(RuntimeException ex) {
        return buildSimpleResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private static ResponseEntity<Object> buildSimpleResponse(String message, HttpStatus status) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }


}
