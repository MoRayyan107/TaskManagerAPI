package com.manager.TaskManagerAPI.repository;

import com.manager.TaskManagerAPI.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * interface having create(), find(), delete(), update() methods
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    // saves new task or updates one
    <S extends Task> S save(S newTask);

    // using Optional to prevent NullPointerException
    Optional<Task> findById(Long id);

    // find all tasks
    List<Task> findAll();

    // list all tasks from title (case-sensitive)
    List<Task> findByTitleContainingIgnoreCase(String title);

    // gets the list of task based on priority
    List<Task> getTaskByPriority(Task.Priority priority);

    // List all task in pagination format
    List<Task> findAll(Sort sort);

    // List all task in pagination
    Page<Task> findAll(Pageable pageable);

    // check if task exists
    boolean existsById(Long id);

    // delete task by ID
    void deleteById(Long id);

    // delete all tasks
    void deleteAll();

    // count number of tasks
    long count();


}
