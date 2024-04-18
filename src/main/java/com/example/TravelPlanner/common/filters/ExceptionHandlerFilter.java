package com.example.TravelPlanner.common.filters;

import com.example.TravelPlanner.common.exceptions.custom.UserNotPartOfTravelPlanException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.EntityNotFoundException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.common.utils.CentralSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, JwtException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            writeResponse(response, "INVALID JWT", HttpStatus.BAD_REQUEST);
        } catch (TravelPlanNotFoundException | UserNotPartOfTravelPlanException e) {
            writeResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            writeResponse(response, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            return "{json conversion error}";
        }
    }

    private void writeResponse(HttpServletResponse response,  String message, HttpStatus httpStatus) throws IOException{
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", message);

        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(body));
    }
}