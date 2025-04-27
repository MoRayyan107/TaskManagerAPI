package com.manager.TaskManagerAPI.controller;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mock;

    @MockitoBean // mocking fake DB
    private TaskService service;

    private Task task;
    private Task task2;

    @BeforeEach
    void setUp() {
        task = new Task("Some Random Task","Description",false);
        task2 = new Task("Some Random Task2","Description2",false);
        task.setID(1L);
        task2.setID(2L);
    }

    @Test
    void testGetAllTasksNonEmptyFile() throws Exception {
        // Arrange
        List<Task> expectedReturn = List.of(task, task2);
        when(service.getAllTasks()).thenReturn(expectedReturn);

        // ac and assert
        mock.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[0].title").value("Some Random Task"),
                        jsonPath("$[1].title").value("Some Random Task2")
                );
    }

    @Test
    void testGetAllTasksEmptyFile() throws Exception {
        // Arrange
        List<Task> expectedReturn = List.of(); // expected an empty list of tasks
        when(service.getAllTasks()).thenReturn(expectedReturn);

        // act and assert
        mock.perform(get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(0)
                );
    }

    @Test
    void TestGetMappingUsingTitleOfTheTask_Success() throws Exception {
        // Arrange
        when(service.searchByTitle("Some"))
                .thenReturn(List.of(task));

        // act and assert
        mock.perform(get("/tasks/search")
                .param("title", "Some")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(1),
                        jsonPath("$[0].title").value("Some Random Task"),
                        jsonPath("$[0].description").value("Description"),
                        jsonPath("$[0].completed").value(false)
                );
    }

    @Test
    void TestGetMappingUsingTitleOfTheTask_Failure() throws Exception {
        // Arrange
        when(service.searchByTitle("Non-existingTask"))
                .thenReturn(List.of());

        // act and assert
        mock.perform(get("/tasks/search")
                .param("title", "Non-existingTask")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
                //Indicating nothing in JSON
    }

    @Test
    void testGetTaskById_Success() throws Exception {
        // Arrange
        when(service.getTaskById(2L))
                .thenReturn(task2);

        // act and assert
        mock.perform(get("/tasks/{id}",2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.title").value("Some Random Task2"),
                        jsonPath("$.description").value("Description2"),
                        jsonPath("$.completed").value(false)
                );
    }

    @Test
    void testGetTaskById_Failure() throws Exception {
        // Arrange
        when(service.getTaskById(4L))
                .thenThrow(new NoSuchElementException());

        // act and assert
        mock.perform(get("/tasks/{id}",4L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void tsetSortTaskByTitle_Success() throws Exception {
        // Arrange
        when(service.getTasksBySort(Sort.by("title").descending()))
                .thenReturn(List.of(task, task2));

        // act and assert
        mock.perform(get("/tasks/sortedByName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[0].title").value("Some Random Task"),
                        jsonPath("$[1].title").value("Some Random Task2")
                );
    }

    @Test
    void tsetSortTaskByTitle_Failure() throws Exception {
        // Arrange
        when(service.getTasksBySort(Sort.by("title").ascending()))
                .thenReturn(List.of());

        // act and assert
        mock.perform(get("/tasks/sortedByName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(0)
                );
    }
}