package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.Assertions;
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
        Assertions.assertEquals("Finish Task manager API", task.getTitle());
        Assertions.assertEquals("Something", task.getDescription());
        Assertions.assertFalse(task.isCompleted());
    }

    @Test
    void testSetters(){
        Task task = new Task("Finish Task manager API", "Something", false);
        task.setTitle("Study 208 Exam");
        task.setDescription("Need to get 70+ in exam");
        task.setCompleted(true); // will happen
        Assertions.assertEquals("Study 208 Exam", task.getTitle());
        Assertions.assertEquals("Need to get 70+ in exam", task.getDescription());
        Assertions.assertTrue(task.isCompleted());
    }
}