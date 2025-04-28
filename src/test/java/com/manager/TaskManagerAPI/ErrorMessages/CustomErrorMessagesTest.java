package com.manager.TaskManagerAPI.ErrorMessages;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class CustomErrorMessagesTest {

    @Test
    void testGetters() {
        CustomErrorMessages test = new CustomErrorMessages(
                "Page Not Found",
                404,
                "Something path"
        );

        assertAll(
                () -> assertEquals("Page Not Found", test.getMessage()),
                () -> assertEquals(404, test.getStatus()),
                () -> assertEquals("Something path", test.getPath())
                // since time can differ no test need for getter
        );
    }
}