package com.manager.TaskManagerAPI.services;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToCreateTask() {
        // arrange
        Task task = new Task("task1 ", "Some description", false);
        when(taskRepository.save(task)).thenReturn(task);

        // act
        Task result = service.createTask(task);
        assertNotNull(result);
        assertEquals("Some description", result.getDescription());
        assertEquals("task1 ", result.getTitle());
        assertFalse(result.isCompleted());

        // verify
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testToGetAllTasks() {
        // arrange
        Task task1 = new Task("Task 1", "Description 1", true);
        Task task2 = new Task("Task 2", "Description 2", false);
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // act
        assertEquals(2, service.getAllTasks().size());

        // verify
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskInSorted(){
        // Arrange
        Task task1 = new Task("Task 2", "Description 1", true);
        Task task2 = new Task("Task 6", "Description 2", false);
        Task task3 = new Task("Task 4", "Description 3", false);
        when(taskRepository.findAll(Sort.by("title").ascending()))
                .thenReturn(Arrays.asList(task1, task3, task2));

        // act
        List<Task> result = service.getTasksBySort(Sort.by("title").ascending());
        assertAll(
                () -> assertEquals(3, result.size()),
                () -> assertEquals(task1 , result.getFirst()),
                () -> assertEquals(task2 , result.getLast()),
                () -> assertEquals(task3 , result.get(1)) // mid value of list
        );

        // verify
        verify(taskRepository, times(1)).findAll(Sort.by("title").ascending());
    }

    @Test
    void testGetTaskById_Success() {
        // Arrange
        Task task1 = new Task("Task 1", "Description 1", true);
        task1.setID(1L); //L as prefix for long data type
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        // act
        assertEquals(1, service.getTaskById(1L).getID());
        Task result = service.getTaskById(1L);
        assertEquals(task1, result);

        // verify
       verify(taskRepository, times(2)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // act
        assertThrows(NoSuchElementException.class, () -> service.getTaskById(1L));

        // verify
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskByTitle_Success() {
        // Arrange
        Task task1 = new Task("Task 1", "Description 1", true);
        when(taskRepository.findByTitleContainingIgnoreCase("Task 1")).thenReturn(Arrays.asList(task1));
        List<Task> result = service.searchByTitle("Task 1");

        // act
        assertEquals(1, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());

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
        Task existing = new Task("Task 1", "Description 1", true);
        Task putUpdate = new Task("Task 2", "Description 2", false);
        Task expected = new Task("Task 2", "Description 2", false);
        existing.setID(id);
        expected.setID(id);
        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(expected); // should get the expected version
        Task result = service.updateTask(id, putUpdate); // make update

        // act
        assertAll(
                () -> assertFalse(result.isCompleted()),
                () -> assertEquals("Task 2", result.getTitle()),
                () -> assertEquals("Description 2", result.getDescription()),
                () -> assertEquals(1, expected.getID())
        );

        // verify
        verify(taskRepository, times(1)).findById(id);
        verify(taskRepository, times(1)).save(existing);

    }

    @Test
    void testUpdateTask_NotFound() {
        // Arrange
        Long id = 1L;
        Task existingTask = new Task("Task 1", "Description 1", true);
        existingTask.setID(id);
        when(taskRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.updateTask(id, existingTask));
    }

    @Test
    void testDeleteTask_Success() {
        // Arrange
        Long id = 1L;
        Task existing = new Task("Task 1", "Description 1", true);
        existing.setID(id);
        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));

        // act
        assertTrue(service.deleteTask(id));

        // verify
        verify(taskRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteTask_NotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // act
        assertThrows(NoSuchElementException.class, () -> service.deleteTask(1L));

        // verify
        verify(taskRepository, times(1)).findById(1L);
    }

}