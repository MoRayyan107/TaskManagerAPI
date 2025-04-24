package com.manager.TaskManagerAPI;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @RequestMapping("/")
    public String home(){
        return "Welcome to my custom Task Manager";
    }

    @Autowired // Automatically injects dependencies
    private TaskRepository repository;

    @GetMapping("/tasks")// maps GET requests
    public List<Task> findAll() {
        return repository.findAll();
    }

    /**
     * maps GET requests by id
     * if found return 200 ok request else
     * return page not found 404
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tasks/sortedByName")
    public List<Task> findAllByName() {
        return repository.findAll(Sort.by("title").descending());
    }

    @GetMapping("/tasks/pagedFormat")
    public Page<Task> findAllByPage(@PageableDefault Pageable pageable) {
        return repository.findAll(pageable);
    }


    @PostMapping("/createTasks") // maps POST requests
    public Task createTask(@Valid @RequestBody Task task) {
        return repository.save(task);
    }

    /**
     * Update task by id
     * if task is updated and saved successfully then 200 ok page
     * if not updated, return 404 page not found
     */
    @PostMapping("/tasks/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskUpdate) {
        return repository.findById(id)
                .map(task -> {
                    task.setTitle(taskUpdate.getTitle());
                    task.setDescription(taskUpdate.getDescription());
                    task.setCompleted(taskUpdate.isCompleted());
                    return ResponseEntity.ok(repository.save(task));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * maps DELETE by id
     * if id is present, then delete it
     * if not, returns 404 page not found
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.notFound().build();
        }
    }
}
