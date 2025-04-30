package com.manager.TaskManagerAPI.services;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.model.TaskHistory;
import com.manager.TaskManagerAPI.repository.TaskHistoryRepository;
import com.manager.TaskManagerAPI.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskHistoryRepository taskHistoryRepository;

    @InjectMocks
    private TaskService service;

    private Task task,task1,task2,task3,task4;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize tasks with different titles, descriptions, and priorities
        task = new Task("Task 0", "Some description", false, Task.Priority.HIGH);
        task1 = new Task("Task 1", "Description 1", true, Task.Priority.HIGH);
        task2 = new Task("Task 2", "Description 2", false, Task.Priority.LOW);
        task3 = new Task("Task 4", "Description 3", false, Task.Priority.LOW);
        task4 = new Task("Task 5", "Description 4", true, Task.Priority.MEDIUM);  // Additional task for testing

        // Set IDs for tasks if needed
        task.setId(1L);
        task1.setId(2L);
        task2.setId(3L);
        task3.setId(4L);
        task4.setId(5L);
    }

    @Test
    void testToCreateTask() {
        // arrange
        when(taskRepository.save(task)).thenReturn(task);

        // act
        Task result = service.createTask(task);
        assertNotNull(result);
        assertEquals("Some description", result.getDescription());
        assertEquals("Task 0", result.getTitle());
        assertEquals(Task.Priority.HIGH, result.getPriority());
        assertFalse(result.isCompleted());

        // verify
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testToGetAllTasks() {
        // arrange
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        // act
        assertEquals(2, service.getAllTasks().size());

        // verify
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskInSorted(){
        // Arrange
        when(taskRepository.findAll(eq(Sort.by("title").ascending())))
                .thenReturn(List.of(task1, task3, task2));

        // act
        List<Task> result = service.getTasksBySort(Sort.by("title").ascending());
        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals(task1 , result.get(0)),
                () -> assertEquals(task3 , result.get(1)), // mid-value of a list
                () -> assertEquals(task2 , result.get(2))
        );

        // verify
        verify(taskRepository, times(1)).findAll(Sort.by("title").ascending());
    }

    @Test
    void testGetTaskById_Success() {
        // Arrange
        when(taskRepository.findById(eq(1L))).thenReturn(Optional.of(task));

        // act
        assertEquals(1, service.getTaskById(1L).getId());
        Task result = service.getTaskById(1L);
        assertEquals(task, result);

        // verify
       verify(taskRepository, times(2)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        // Arrange
        when(taskRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // act
        assertThrows(NoSuchElementException.class, () -> service.getTaskById(1L));

        // verify
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByTitle_Success() {
        // Arrange
        when(taskRepository.findByTitleContainingIgnoreCase("Task 1")).thenReturn(List.of(task1));
        List<Task> result = service.searchByTitle("Task 1");

        // act
        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());

        // verify
        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase("Task 1");
    }

    @Test
    void testGetTaskByTitle_NotFound() {
        // Arrange
        when(taskRepository.findByTitleContainingIgnoreCase("Task 1")).thenReturn(Collections.emptyList());

        // act
        assertThrows(NoSuchElementException.class, () -> service.searchByTitle("Task 1"));

        // verify
        verify(taskRepository, times(1)).findByTitleContainingIgnoreCase("Task 1");
    }

    @Test
    void testUpdateTask_Success() {
        // Arrange
        Long id = 1L;
        Task existing = new Task("Task 1", "Description 1", true, Task.Priority.HIGH);
        Task expected = new Task("Task 2", "Description 2", false, Task.Priority.MEDIUM);
        existing.setId(id);
        expected.setId(id);
        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(existing));

        // save the history of updated task
        String changedAction = "Task Title changed to -> '" + expected.getTitle() + "'";
        TaskHistory historyTask = new TaskHistory(existing,changedAction.trim(), LocalDateTime.now());
        when(taskHistoryRepository.save(any(TaskHistory.class))).thenReturn(historyTask);

        // ensure to return the updated task
        when(taskRepository.save(any(Task.class))).thenReturn(expected);
        Task result = service.updateTask(id, expected); // make update

        // act
        assertAll(
                () -> assertFalse(result.isCompleted()),
                () -> assertEquals("Task 2", result.getTitle()),
                () -> assertEquals("Description 2", result.getDescription()),
                () -> assertEquals(Task.Priority.MEDIUM, result.getPriority())
        );

        // verify
        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).save(existing);
        verify(taskHistoryRepository, times(1)).save(any(TaskHistory.class));

    }

    @Test
    void testUpdateTask_NotFound() {
        // Arrange
        Long id = 1L;
        Task existingTask = new Task("Task 1", "Description 1", true, Task.Priority.HIGH);
        existingTask.setId(id);
        when(taskRepository.findById(eq(id))).thenReturn(Optional.empty());

        // act
        assertThrows(NoSuchElementException.class, () -> service.updateTask(id, existingTask));

        // verify
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteTask_Success() {
        // Arrange
        Long id = 1L;
        Task existing = new Task("Task 1", "Description 1", true, Task.Priority.HIGH);
        existing.setId(id);
        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(existing));

        // act
        assertTrue(service.deleteTask(id));

        // verify
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteTask_NotFound() {
        // Arrange
        when(taskRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // act
        assertThrows(NoSuchElementException.class, () -> service.deleteTask(1L));

        // verify
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByPriority_Success() {
        // Arrange
        when(taskRepository.getTaskByPriority(eq(Task.Priority.LOW))).thenReturn(List.of(task2,task3));
        List<Task> result = service.getTaskPriority(Task.Priority.LOW);

        // assert
        assertAll(
                () -> assertEquals(2,result.size()),
                () -> assertEquals(task2.getPriority(), result.get(0).getPriority()),
                () -> assertEquals(task3.getPriority(), result.get(1).getPriority())
        );

        // verify
        verify(taskRepository, times(1)).getTaskByPriority(Task.Priority.LOW);
    }

    @Test
    void testMultiplePriorities(){
        // arrange
        when(taskRepository.getTaskByPriority(eq(Task.Priority.HIGH))).thenReturn(List.of(task,task1));
        when(taskRepository.getTaskByPriority(eq(Task.Priority.MEDIUM))).thenReturn(List.of(task4));

        List<Task> priorityHigh = service.getTaskPriority(Task.Priority.HIGH);
        List<Task> priorityMedium = service.getTaskPriority(Task.Priority.MEDIUM);

        // act
        assertAll(
                () -> assertEquals(2, priorityHigh.size()),
                () -> assertEquals(task, priorityHigh.getFirst()),
                () -> assertEquals(task1, priorityHigh.get(1)),
                () -> assertEquals(1, priorityMedium.size()),
                () -> assertEquals(task4, priorityMedium.getFirst())
        );

        // verify
        verify(taskRepository, times(1)).getTaskByPriority(Task.Priority.HIGH);
        verify(taskRepository, times(1)).getTaskByPriority(Task.Priority.MEDIUM);
    }

    @Test
    void testGetTaskByPriority_NotFound() {
        // arrange
        when(taskRepository.getTaskByPriority(eq(Task.Priority.MEDIUM))).thenReturn(List.of());

        // act
        assertThrows(NoSuchElementException.class, () -> service.getTaskPriority(Task.Priority.MEDIUM));

        // verify
        verify(taskRepository, times(1)).getTaskByPriority(Task.Priority.MEDIUM);
    }

}