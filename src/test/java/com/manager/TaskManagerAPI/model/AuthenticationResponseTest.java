package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationResponseTest {

    private AuthenticationResponse authenticationResponse;

    @BeforeEach
    void setUp() {
        authenticationResponse = new AuthenticationResponse("Some Random Token");
    }

    @Test
    void getToken() {
        assertEquals("Some Random Token",authenticationResponse.getJwt());
    }

}