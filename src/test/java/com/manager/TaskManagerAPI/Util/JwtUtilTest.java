package com.manager.TaskManagerAPI.Util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static com.manager.TaskManagerAPI.constants.AppConstants.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private UserDetails userDetails;
    private JwtUtil jwtUtil;

    private static final String USERNAME = "TestUser";
    private static final String PASSWORD = "TestPassword";

    @BeforeEach
    void setUp(){
        jwtUtil = new JwtUtil();
        userDetails = new User(USERNAME, PASSWORD, List.of(new SimpleGrantedAuthority(ROLE_USER)));
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken(userDetails);
        String extractUsername = jwtUtil.extractUsername(token);
        Boolean isValid = jwtUtil.validateToken(token, userDetails);
        assertAll(
                () -> assertNotNull(token),
                () -> assertEquals(USERNAME, extractUsername),
                () -> assertTrue(isValid)
        );
    }

    @Test
    void testInvalidTokenWithWrongUsername(){
        String token = jwtUtil.generateToken(userDetails);

        UserDetails userDetails1 = new User("WrongUsername", PASSWORD, List.of());
        assertFalse(jwtUtil.validateToken(token, userDetails1));
    }

}