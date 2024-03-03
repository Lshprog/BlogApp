package com.example.TravelPlanner.common.config;

import com.example.TravelPlanner.auth.UserRepository;
import com.example.TravelPlanner.auth.entities.CustomUserDetails;
import com.example.TravelPlanner.common.utils.MapperUtil;
import com.example.TravelPlanner.travelplanning.dto.EventCreateDTO;
import com.example.TravelPlanner.travelplanning.dto.EventDTO;
import com.example.TravelPlanner.travelplanning.dto.VoteDTO;
import com.example.TravelPlanner.travelplanning.dto.VotingDTO;
import com.example.TravelPlanner.travelplanning.entities.Event;
import com.example.TravelPlanner.travelplanning.entities.Vote;
import com.example.TravelPlanner.travelplanning.entities.Voting;
import com.example.TravelPlanner.travelplanning.repositories.TravelPlanRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> modelMapper.map(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")), CustomUserDetails.class);
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

        ModelMapper modelMapper = new ModelMapper();

//        TypeMap<Event, EventDTO> propertyMapperEvent = modelMapper.createTypeMap(Event.class, EventDTO.class);
//         // add deep mapping to flatten source's Player object into a single field in destination
//        propertyMapperEvent.addMappings(
//                mapper -> mapper.map(src -> src.getCreator().getUsername(), EventDTO::setCreator)
//        );
//        TypeMap<Voting, VotingDTO> propertyMapperVoting = modelMapper.createTypeMap(Voting.class, VotingDTO.class);
//        // add deep mapping to flatten source's Player object into a single field in destination
//        propertyMapperVoting.addMappings(
//                mapper -> mapper.map(src -> src.getCreator().getUsername(), VotingDTO::setCreator)
//        );
//        modelMapper.addMappings(new PropertyMap<Event, EventDTO>() {
//            @Override
//            protected void configure() {
//                map().setCreator(source.getCreator().getUsername());
//                map().setTravelPlanId(source.getTravelPlan().getId());
//                map().setLoc(source.getLoc());
//            }
//        });
//        modelMapper.addMappings(new PropertyMap<Voting, VotingDTO>() {
//            @Override
//            protected void configure() {
//                map().setCreator(source.getCreator().getUsername());
//            }
//        });
        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }


}
