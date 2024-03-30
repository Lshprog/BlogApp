package com.example.TravelPlanner.common.config;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.AuthUserDetails;
import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.travelplanning.dto.event.EventDTO;
import com.example.TravelPlanner.travelplanning.dto.voting.VotingDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
    private final UserRepository userRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Value("${redis_hostname}")
    private String redis_hostname;

    @Value("${redis_port}")
    private Integer redis_port;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> modelMapper.map(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")), AuthUserDetails.class);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {

        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redis_hostname, redis_port);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }


}
