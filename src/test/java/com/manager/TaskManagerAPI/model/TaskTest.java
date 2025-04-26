package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @BeforeEach
    void setUp() {
        Task task = new Task();
    }

    @Test
    void testGetters(){
        Task task = new Task("Finish Task manager API", "Something", false);
        assertAll(
        () -> assertEquals("Finish Task manager API", task.getTitle()),
        () -> assertEquals("Something", task.getDescription()),
        () -> assertFalse(task.isCompleted())
        );
    }

    @Test
    void testSetters(){
        Task task = new Task("Finish Task manager API", "Something", false);
        task.setTitle("Study 208 Exam");
        task.setDescription("Need to get 70+ in exam");
        task.setCompleted(true); // will happen
        assertAll(
                () -> assertEquals("Study 208 Exam", task.getTitle()),
                () -> assertEquals("Need to get 70+ in exam", task.getDescription()),
                () -> assertTrue(task.isCompleted())
        );
    }
}