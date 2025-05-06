package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser("Something123","RandomPassword","John","Doe","JohnDoe123@gmail.com");
    }

    @Test
    void testGetters(){
        assertAll(
                () -> assertNull(appUser.getId()),
                () -> assertEquals("Something123", appUser.getUsername()),
                () -> assertEquals("RandomPassword", appUser.getPassword()),
                () -> assertEquals("ROLE_USER",appUser.getRole()),
                () -> assertEquals("John", appUser.getFirstName()),
                () -> assertEquals("Doe", appUser.getLastName()),
                () -> assertEquals("JohnDoe123@gmail.com",appUser.getEmail())
        );
    }

    @Test
    void testSetters(){
        appUser.setId(5L);
        appUser.setUsername("New User");
        appUser.setPassword("New Password");
        appUser.setFirstName("New First Name");
        appUser.setLastName("New Last Name");
        appUser.setEmail("New Email");
        appUser.setRole("New Role");

        assertAll(
                () -> assertEquals(5, appUser.getId()),
                () -> assertEquals("New User", appUser.getUsername()),
                () -> assertEquals("New Password", appUser.getPassword()),
                () -> assertEquals("New Role",appUser.getRole()),
                () -> assertEquals("New First Name", appUser.getFirstName()),
                () -> assertEquals("New Last Name", appUser.getLastName()),
                () -> assertEquals("New Email",appUser.getEmail())
        );

    }

}