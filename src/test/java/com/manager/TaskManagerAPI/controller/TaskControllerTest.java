package com.manager.TaskManagerAPI.controller;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.model.TaskHistory;
import com.manager.TaskManagerAPI.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mock;

    @MockitoBean // mocking fake DB
    private TaskService service;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Task task;
    private Task task2;

    @BeforeEach
    void setUp() {
        task = new Task("Some Random Task","Description",false,Task.Priority.LOW);
        task2 = new Task("Some Random Task2","Description2",false, Task.Priority.HIGH);
        task.setId(1L);
        task2.setId(2L);
    }

    @Test
    void testCreateTask_Success() throws Exception {
        // Arrange
        when(service.createTask(any(Task.class))).thenReturn(task);

        // act and assert
        mock.perform(post("/api/tasks/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(task.toString()))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.title").value("Some Random Task")
                );
    }

    @Test
    void testCreateTask_NullValues() throws Exception {
        // arrange
        String invalidTaskJson = """
                {
                    \"title\": null,
                    \"description\": \"Something\",
                    \"completed\": false
                }
                """;

        //act and assert
        mock.perform(post("/api/tasks/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidTaskJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllTasks_Success() throws Exception {
        // Arrange
        List<Task> expectedReturn = List.of(task, task2);
        when(service.getAllTasks()).thenReturn(expectedReturn);

        // ac and assert
        mock.perform(get("/api/tasks/allTasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[0].title").value("Some Random Task"),
                        jsonPath("$[1].title").value("Some Random Task2")
                );
    }

    @Test
    void testGetAllTasks_EmptyList() throws Exception {
        // Arrange
        when(service.getAllTasks()).thenReturn(List.of());

        // act and assert
        mock.perform(get("/api/tasks/allTasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
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
        mock.perform(get("/api/tasks/search")
                .param("title", "Some")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(1),
                        jsonPath("$[0].title").value("Some Random Task"),
                        jsonPath("$[0].description").value("Description"),
                        jsonPath("$[0].completed").value(false)
                );

        verify(service, times(1)).searchByTitle("Some");
    }

    @Test
    void TestGetMappingUsingTitleOfTheTask_EmptyList() throws Exception {
        // Arrange
        when(service.searchByTitle("Non-existingTask"))
                .thenReturn(List.of());

        // act and assert
        mock.perform(get("/api/tasks/search")
                .param("title", "Non-existingTask")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
                //Indicating nothing in JSON
    }

    @Test
    void testGetTaskById_Success() throws Exception {
        // Arrange
        when(service.getTaskById(eq(2L)))
                .thenReturn(task2);

        // act and assert
        mock.perform(get("/api/tasks/{id}",2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
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
        when(service.getTaskById(eq(4L)))
                .thenThrow(new NoSuchElementException());

        // act and assert
        mock.perform(get("/api/tasks/{id}",4L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void tsetSortTaskByTitle_Success() throws Exception {
        // Arrange
        when(service.getTasksBySort(Sort.by("title").ascending()))
                .thenReturn(List.of(task, task2));

        // act and assert
        mock.perform(get("/api/tasks/sorted")
                        .param("sort", "title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[0].title").value("Some Random Task"),
                        jsonPath("$[1].title").value("Some Random Task2")
                );
    }

    @Test
    void tsetSortTaskByTitle_EmptyList() throws Exception {
        // Arrange
        when(service.getTasksBySort(Sort.by("title").ascending()))
                .thenReturn(List.of());

        // act and assert
        mock.perform(get("/api/tasks/sorted")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(0)
                );
    }

    @Test
    void testDeleteTask_Success() throws Exception {
        // Arrange
        when(service.deleteTask(eq(2L))).thenReturn(true);

        // act and assert
        mock.perform(delete("/api/tasks/delete/{id}",2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteTask_Failure() throws Exception {
        // Arrange
        when(service.deleteTask(eq(2L))).thenThrow(new NoSuchElementException());

        // act and assert
        mock.perform(delete("/api/tasks/delete/{id}",2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatingTask_Success() throws Exception {
        // Arrange
        // task = "Some Random Task","Description",false,Task.Priority.LOW
        Task updatingTask = new Task(
                "CS208 Exam",
                "Done Well maybe 44/60",
                true,Task.Priority.HIGH);
        when(service.updateTask(eq(1L), any(Task.class))).thenReturn(updatingTask);

        // act and assert
        mock.perform(post("/api/tasks/update/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "CS208 Exam",
                        "description": "Done Well maybe 44/60",
                        "completed": true
                    }
                    """))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.title").value("CS208 Exam"),
                        jsonPath("$.description").value("Done Well maybe 44/60"),
                        jsonPath("$.completed").value(true)
                );
    }

    @Test
    void testUpdatingTask_Failure() throws Exception {
        // Arrange
        Task updatingTask = new Task("CS208 Exam","Done Well maybe 44/60",true,Task.Priority.HIGH);
        when(service.updateTask(eq(1L), any(Task.class)))
                .thenThrow(new NoSuchElementException());

        // act and arrange
        mock.perform(post("/api/tasks/update/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "CS208 Exam",
                        "description": "Done Well maybe 44/60",
                        "completed": true
                    }
                    """))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetTasksHistory_Success() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.of(2022, 1, 1, 12, 0);
        Task updateTask = new Task("CS208 Exam","Done Well maybe 44/60",true,Task.Priority.HIGH);
        TaskHistory taskHistory1 = new TaskHistory(task,"Created Task!",now);
        TaskHistory taskHistory2 = new TaskHistory(updateTask,"Updated Task!",now);

        when(service.updateTask(eq(1L), any(Task.class))).thenReturn(updateTask);
        when(service.getTaskHistory(1L)).thenReturn(List.of(taskHistory1,taskHistory2));

        // Act and Assert
        mock.perform(get("/api/tasks/history/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.size()").value(2),
                        jsonPath("$[0].task.title").value(task.getTitle()),
                        jsonPath("$[1].task.title").value(updateTask.getTitle())
                );
    }

}