package com.example.TravelPlanner.common.filters;

import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.common.exceptions.custom.UserNotPartOfTravelPlanException;
import com.example.TravelPlanner.common.exceptions.custom.entitynotfound.TravelPlanNotFoundException;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.example.TravelPlanner.travelplanning.repositories.UserPlanRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelPlanMembershipFilter extends OncePerRequestFilter {

    private final UserPlanRepository userPlanRolesRepository;

    private final TravelPlanRepository travelPlanRepository;
    private final Pattern excludePattern = Pattern.compile("^/api/v1/travelplans/join(/|$)");
    private final Pattern includePattern = Pattern.compile("^/api/v1/travelplans/(\\d+)(/|$)");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        Matcher matcher = includePattern.matcher(path);
        if (matcher.find()) {
            Long travelPlanId = Long.parseLong(matcher.group(1));
            if (!travelPlanRepository.existsById(travelPlanId)) {
                throw new TravelPlanNotFoundException(travelPlanId);
            }
            CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(!userPlanRolesRepository.existsByUserIdAndTravelPlanId(principal.getId(), travelPlanId)){
                throw new UserNotPartOfTravelPlanException();
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getServletPath();
        log.info("in shouldNotFilter");
        Matcher matcher = excludePattern.matcher(path);
        return matcher.matches();
    }
}
