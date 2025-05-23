package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private Task task;
    @BeforeEach
    void setUp() {
        task = new Task("Finish Task manager API", "Something", false, Task.Priority.HIGH);
        task.setId(1L);
    }

    @Test
    void testGetters(){
        String expectedString = task.toString();
        assertAll(
                () -> assertEquals("Finish Task manager API", task.getTitle()),
                () -> assertEquals("Something", task.getDescription()),
                () -> assertFalse(task.isCompleted()),
                () -> assertEquals(expectedString, task.toString()),
                () -> assertEquals(Task.Priority.HIGH, task.getPriority())
        );
    }

    @Test
    void testSetters(){
        task.setTitle("Study 208 Exam");
        task.setDescription("Need to get 70+ in exam");
        task.setCompleted(true); // will happen
        task.setPriority(Task.Priority.LOW);
        assertAll(
                () -> assertEquals("Study 208 Exam", task.getTitle()),
                () -> assertEquals("Need to get 70+ in exam", task.getDescription()),
                () -> assertTrue(task.isCompleted()),
                () -> assertEquals(Task.Priority.LOW, task.getPriority())
        );
    }
}