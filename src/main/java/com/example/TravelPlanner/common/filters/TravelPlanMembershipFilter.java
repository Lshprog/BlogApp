package com.example.TravelPlanner.common.filters;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRolesRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Filter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TravelPlanMembershipFilter extends OncePerRequestFilter {

    private final UserPlanRolesRepository userPlanRolesRepository;
    private static final Pattern travelPlanIdPattern = Pattern.compile("^/api/v1/(\\d+)/.*");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        Matcher matcher = travelPlanIdPattern.matcher(path);
        if (matcher.find()) {
            Long travelPlanId = Long.parseLong(matcher.group(1));
            CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(userPlanRolesRepository.existsByUserIdAndTravelPlanId(travelPlanId, principal.getId())){
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not part of the travel plan");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        return !path.startsWith("/api/v1/travel_plan/");
    }
}
