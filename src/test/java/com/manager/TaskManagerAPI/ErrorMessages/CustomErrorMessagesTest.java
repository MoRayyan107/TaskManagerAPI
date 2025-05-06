package com.manager.TaskManagerAPI.ErrorMessages;

import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CustomErrorMessagesTest {

    @Test
    void testGetters() {
        LocalDateTime date = LocalDateTime.of(2025,4,12,7,35);
        CustomErrorMessages test = new CustomErrorMessages(
                "Page Not Found",
                404,
                "Something path"
        );
        test.setDate(date);


        assertAll(
                () -> assertEquals("Page Not Found", test.getMessage()),
                () -> assertEquals(404, test.getStatus()),
                () -> assertEquals("Something path", test.getPath()),
                () -> assertEquals(date, test.getDate())
        );
    }
}