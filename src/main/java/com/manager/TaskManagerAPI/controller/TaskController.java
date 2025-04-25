package com.manager.TaskManagerAPI.controller;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling task-related operations.
 * @author Mohammad Rayyan
 */
@RestController
public class TaskController {

    /**
     * Home route for displaying a message
     * @returns a string Welcome message
     */
    @RequestMapping("/")
    public String home(){
        return "Welcome to my custom Task Manager";
    }

    @Autowired // Automatically injects dependencies
    private TaskService service;

    /**
     * retrieves all the tasks
     * @return list of tasks
     */
    @GetMapping("/tasks")// maps GET requests
    public List<Task> findAll() {
        return service.getAllTasks();
    }

    /**
     * retrieves tasks based on ID
     * @param id ID of the task to retrieve
     * @return ResponseEntity giving us OK for 200, or 404 if NOT FOUND
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task task = service.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * retrieves tasks in ordered by title of the task
     * @return sorted list of tasks by title
     */
    @GetMapping("/tasks/sortedByName")
    public List<Task> findAllByName() {
        return service.getTasksBySort(Sort.by("title").descending());
    }

    /**
     * retrieves tasks in pageable format
     * @param pageable the pagination information
     * @return paged list of tasks
     */
    @GetMapping("/tasks/pagedFormat")
    public Page<Task> findAllByPage(@PageableDefault Pageable pageable) {
        return service.getTasksPageable(pageable);
    }

    /**
     * creates a new tasks
     * @param task task to create (needs to be a valid form)
     * @return creates a task
     */
    @PostMapping("/tasks") // maps POST requests
    public Task createTask(@Valid @RequestBody Task task) {
        return service.createTask(task);
    }

    /**
     * Updates the task based on ID
     * @param id task id to update
     * @param taskUpdate new task to replace with an old task based on id
     * @return updated task wrapped around ResponseEntity
     */
    @PostMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskUpdate) {
        Task task = service.updateTask(id, taskUpdate);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Deletes a task based on ID
     * @param id task ID to be deleted
     * @return ResponseEntity giving us 200 OK or 404 NOT FOUND
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * searches task based on title
     * @param title name of the task to be searched
     * @return list of task from title
     */
    @GetMapping("/tasks/search")
    public List<Task> searchTask(@RequestParam String title) {
        return service.searchByTitle(title);
    }
}
