package com.manager.TaskManagerAPI.services;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * constructor injection
     * @param taskRepository repository to manage task data
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * creates a task
     * @param task pass in the task to be created
     * @return the saved task
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * gets all task
     * @return all task in a list
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * sorts the list of tasks
     * @param sort holds what type of sorting (sort by title, id etc.)
     * @return all task in sorted manner
     */
    public List<Task> getTasksBySort(Sort sort) {
        return taskRepository.findAll(sort);
    }

    /**
     * gets all task in paged format
     * @param pageable pageable information
     * @return task in pageable format
     */
    public Page<Task> getTasksPageable(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    /**
     * gets task by ID
     * @param id to to search by task ID
     * @return task of the given task ID
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

    /**
     * gets the task from title
     * @param title title of the task to search
     * @return list of task withh the same name (case-sensitive)
     */
    public List<Task> searchByTitle(String title){
        List<Task> titleTask =  taskRepository.findByTitleContainingIgnoreCase(title);
        if (titleTask.isEmpty()){
            throw new NoSuchElementException("Task not found for title: "+title);
        }
        return titleTask;
    }

    /**
     * updates a current task
     * @param id task ID to update
     * @param newTask the new task to update the old task
     * @return the updated task with new task
     */
    public Task updateTask(Long id, Task newTask) {
        return taskRepository.findById(id).map(
                existingTask -> {
                    existingTask.setTitle(newTask.getTitle());
                    existingTask.setDescription(newTask.getDescription());
                    existingTask.setCompleted(newTask.isCompleted());
                    return taskRepository.save(existingTask);
                }).orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

    /**
     * deletes a task from task ID
     * @param id ID to delete the task
     * @return true if deletion is a success else false
     */
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                }).orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

}
