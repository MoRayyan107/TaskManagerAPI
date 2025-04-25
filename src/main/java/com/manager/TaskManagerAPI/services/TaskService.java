package com.manager.TaskManagerAPI.services;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ssl.NoSuchSslBundleException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class TaskService {

    // wires the Repository with Service class
    @Autowired
    private TaskRepository taskRepository;

    // creates a task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // gets all the tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // get tasks in sorted form
    public List<Task> getTasksBySort(Sort sort) {
        return taskRepository.findAll(sort);
    }

    // get tasks in pageable form
    public Page<Task> getTasksPageable(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    // get tasks by ID
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

    // Find tasks containing a keyword (case-insensitive)
    public List<Task> searchByTitle(String title){
        List<Task> titleTask =  taskRepository.findByTitleContainingIgnoreCase(title);
        if (titleTask.isEmpty()){
            throw new NoSuchElementException("Task not found for title: "+title);
        }
        return titleTask;
    }


    public Task updateTask(Long id, Task newTask) {
        return taskRepository.findById(id).map(
                existingTask -> {
                    existingTask.setTitle(newTask.getTitle());
                    existingTask.setDescription(newTask.getDescription());
                    existingTask.setCompleted(newTask.isCompleted());
                    return taskRepository.save(existingTask);
                }).orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                }).orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

}
