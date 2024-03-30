package com.example.TravelPlanner.auth;

import com.example.TravelPlanner.auth.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

//    @Test
//    @Transactional
//    public void testSaveUser() {
//        User saveduser = userRepository.save(User.builder()
//                .username("Alext")
//                .email("tt@gmail.com")
//                .username("strong")
//                .build());
//
//        System.out.println("before checks");
//        assertThat(saveduser).isNotNull();
//        assertThat(saveduser.getId()).isNotNull();
//    }
//
//    @Test
//    public void testFindUserByUsername() {
////        Optional<User> user = userRepository.findByUsername("Alext");
////        System.out.println("before checks");
////        assertThat(user).isNotNull();
////        assertThat(user.get().getId()).isNotNull();
//    }



}