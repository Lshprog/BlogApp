package com.example.TravelPlanner.auth;

import com.example.TravelPlanner.auth.common.AuthenticationRequest;
import com.example.TravelPlanner.auth.common.AuthenticationResponse;
import com.example.TravelPlanner.auth.common.RegisterRequest;
import com.example.TravelPlanner.auth.config.JwtService;
import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.auth.entities.Role;
import com.example.TravelPlanner.auth.entities.User;
import com.example.TravelPlanner.common.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(MapperUtil.map(user, CustomUserDetails.class));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if(user.isEmpty()){
            throw new NoSuchElementException();
        }
        var jwtToken = jwtService.generateToken(MapperUtil.map(user, CustomUserDetails.class));
        return AuthenticationResponse.builder().token(jwtToken).build();

    }
}
