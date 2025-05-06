package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationRequestTest {

    private AuthenticationRequest authenticationRequest;

    @BeforeEach
    void setUp() {
        authenticationRequest = new AuthenticationRequest("username", "password");
    }

    @Test
    void testGetters() {
        assertEquals("username", authenticationRequest.getUsername());
        assertEquals("password", authenticationRequest.getPassword());
    }

    @Test
    void testSetters(){
        authenticationRequest.setUsername("New username");
        authenticationRequest.setPassword("New password");
        assertEquals("New username", authenticationRequest.getUsername());
        assertEquals("New password", authenticationRequest.getPassword());
    }

}