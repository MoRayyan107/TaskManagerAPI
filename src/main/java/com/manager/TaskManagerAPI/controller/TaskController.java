package com.manager.TaskManagerAPI.controller;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.model.TaskHistory;
import com.manager.TaskManagerAPI.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.manager.TaskManagerAPI.constants.AppConstants.*;

/**
 * REST controller for handling task-related operations.
 * @author Mohammad Rayyan
 */
@RestController
@Tag(name = "Task Controller")
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

//    /**
//     * Home route for displaying a message
//     * @returns a string Welcome message
//     */
//    @RequestMapping("/")
//    public String home(){
//        return "Welcome to my custom Task Manager";
//    }


    /**
     * Constructor Injection
     * @param service managing the task service
     */
    @Autowired
    public TaskController(TaskService service) {
        this.service = service;
    }

    /**
     * retrieves all the tasks
     * @return list of tasks
     */
    @Operation(summary = "gets all task from DB (Authentication Required if using Postman)", description = "returns a list of tasks")
    @GetMapping("/allTasks")// maps GET requests
    public ResponseEntity<List<Task>> findAll() {
        List<Task> getTask =  service.getAllTasks();
        logger.info("Fetching all Tasks....");
        return new ResponseEntity<>(getTask, HttpStatus.OK);
    }

    /**
     * 
     * @param priority
     * @return
     */
    @Operation(summary = "returns tasks based on priority (Authentication Required if using Postman)", description = "returns a list of task based on priority")
    @GetMapping("/priority")
    public ResponseEntity<List<Task>> findAllByPriority(@RequestParam Task.Priority priority) {
        List<Task> tasks = service.getTaskPriority(priority);
        logger.info("Fetching Tasks based on priority: {}", priority);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * retrieves tasks in ordered by title of the task
     * @param sortBy input of sorting wither by title, id or description of a task
     * @return sorted list of tasks by title
     */
    @Operation(summary = "gets tasks in sorted manner based on title or description (Authentication Required if using Postman)", description = "returns a list of sorted tasks")
    @GetMapping("/sorted")
    public ResponseEntity<List<Task>> findAllSorted(@RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy) {
        List<Task> sortedTasks =  service.getTasksBySort(Sort.by(sortBy).ascending());
        logger.info("Fetching Tasks based on sort by: {}", sortBy);
        return new ResponseEntity<>(sortedTasks, HttpStatus.OK);
    }

    /**
     * retrieves tasks in pageable format
     * @param pageable the pagination information
     * @return paged list of tasks
     */
    @Operation(summary = "gets tasks in paged format (Authentication Required if using Postman)", description = "returns a paged fom of tasks")
    @GetMapping("/page")
    public ResponseEntity<Page<Task>> findAllByPage(@PageableDefault(
            page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable pageable)
    {
        Page<Task> page =  service.getTasksPageable(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * creates a new tasks
     * @param task task to create (needs to be a valid form)
     * @return creates a task
     */
    @Operation(summary = "create a task (Authentication Required if using Postman)", description = "returns an object of created task")
    @PostMapping("/create") // maps POST requests
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task createdTask = service.createTask(task);
        logger.info("Created Task: {}", createdTask);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Updates the task based on ID
     * @param id task id to update
     * @param taskUpdate new task to replace with an old task based on id
     * @return updated task wrapped around ResponseEntity
     */
    @Operation(summary = "updates the task by given ID (Authentication Required if using Postman)", description = "returns the task updated object")
    @PostMapping("/update/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskUpdate) {
        Task task = service.updateTask(id, taskUpdate);
        logger.info("Updated Task: {}", task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Deletes a task based on ID
     * @param id task ID to be deleted
     * @return ResponseEntity giving us 200 OK or 404 NOT FOUND
     */
    @Operation(summary = "deletes the task based on ID (Authentication Required if using Postman)", description = "returns nothing just a 200 OK message")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        logger.info("Deleted Task: {}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * searches task based on title
     * @param title name of the task to be searched
     * @return list of a task from title
     */
    @Operation(summary = "gets the task based on either priority, description or title (Authentication Required if using Postman)", description = "returns a list of tasks based on either priority, description or title")
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTask(@RequestParam String title) {
        List<Task> searchByTitle = service.searchByTitle(title);
        logger.info("Fetching Tasks based on title: {}", title);
        return new ResponseEntity<>(searchByTitle, HttpStatus.OK);
    }

    /**
     * retrieves tasks based on ID
     * @param id ID of the task to retrieve
     * @return ResponseEntity giving us OK for 200, or 404 if NOT FOUND
     */
    @Operation(summary = "finds the task by ID (Authentication Required if using Postman)", description = "return the task object from ID input")
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        Task task = service.getTaskById(id);
        logger.info("Fetching Task: {}", task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * pageable format of task history
     * @param id Task id to get the history
     * @return page formated of a task
     */
    @Operation(summary = "History of changes for a particular task (Authentication Required if using Postman)", description = "returns a list of changes for a particular task")
    @GetMapping("/history/{id}")
    public ResponseEntity<List<TaskHistory>> taskHistoryPageable(@PathVariable Long id) {
        List<TaskHistory> pagedTaskHistory = service.getTaskHistory(id);
        return new ResponseEntity<>(pagedTaskHistory, HttpStatus.OK);
    }

}
