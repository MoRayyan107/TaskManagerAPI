package com.manager.TaskManagerAPI.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskHistoryTest {

    private TaskHistory taskHistory;
    private Task task, taskToSet;
    private LocalDateTime fixedTime, updatedTime;

    @BeforeEach
    void setUp() {
        task = new Task("New Task","No Description",false,Task.Priority.LOW);
        task.setID(1L);
        taskToSet = new Task("Updated title","No Description",false,Task.Priority.LOW);
        taskToSet.setID(1L);

        fixedTime = LocalDateTime.of(2025,4,12,7,35);
        taskHistory = new TaskHistory(task,"new task added", fixedTime);
    }

    @Test
    void testGetters() {
        assertAll(
                () -> assertEquals(1, taskHistory.getTask().getID()),
                () -> assertEquals(task, taskHistory.getTask()),
                () -> assertEquals("new task added", taskHistory.getChangedAction()),
                () -> assertEquals(fixedTime, taskHistory.getTime())
        );
    }

    @Test
    void testSetters() {
        // to set new time to update
        updatedTime = LocalDateTime.of(2025,4,12,12,3);

        taskHistory.setTask(taskToSet);
        taskHistory.setChangedAction("updated title");
        taskHistory.setTime(updatedTime);
        assertAll(
                () -> assertEquals(taskToSet, taskHistory.getTask()),
                () -> assertEquals("updated title", taskHistory.getChangedAction()),
                () -> assertEquals(updatedTime, taskHistory.getTime())
        );
    }

}