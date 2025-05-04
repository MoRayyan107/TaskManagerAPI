package com.manager.TaskManagerAPI.services;

import com.manager.TaskManagerAPI.model.Task;
import com.manager.TaskManagerAPI.model.TaskHistory;
import com.manager.TaskManagerAPI.repository.TaskHistoryRepository;
import com.manager.TaskManagerAPI.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskHistoryRepository taskHistoryRepository;

    /**
     * constructor injection
     * @param taskRepository repository to manage task data
     */
    @Autowired
    public TaskService(TaskRepository taskRepository, TaskHistoryRepository taskHistoryRepository) {
        this.taskRepository = taskRepository;
        this.taskHistoryRepository = taskHistoryRepository;
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
     * gets the task based on priority
     * @param priority Enum of either High Low or Medium
     * @return list opf task based on the highest priority
     */
    public List<Task> getTaskPriority(Task.Priority priority) {
        List<Task> result = taskRepository.getTaskByPriority(priority);
        if (result.isEmpty()) {
            throw new NoSuchElementException("No task found with priority " + priority);
        }
        return result;
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
        // get the task
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No task found for id:" + id));

        // check which property is changed
        StringBuilder changedAction  = new StringBuilder();
        if (!existingTask.getTitle().equals(newTask.getTitle())){
            changedAction.append("Task Title changed to -> '").append(newTask.getTitle()).append("', ");
        }
        if (!existingTask.getDescription().equals(newTask.getDescription())){
            changedAction.append("Task Description changed to -> '").append(newTask.getDescription()).append("', ");
        }
        if (existingTask.getPriority() != newTask.getPriority()){
            changedAction.append("Task Priority changed to -> '").append(newTask.getPriority()).append("', ");
        }
        if (existingTask.isCompleted() != newTask.isCompleted()){
            String completed = (newTask.isCompleted()) ? "Yes" : "No";
            changedAction.append("Task Completed changed to -> '").append(completed).append("'. ");
        }

        // log in the history of updated task
        if (!changedAction.isEmpty()){
            TaskHistory logHistory = new TaskHistory(existingTask,changedAction.toString(), LocalDateTime.now());
            taskHistoryRepository.save(logHistory);
        }

        // update th task
        existingTask.setTitle(newTask.getTitle());
        existingTask.setDescription(newTask.getDescription());
        existingTask.setCompleted(newTask.isCompleted());
        existingTask.setPriority(newTask.getPriority());

        return taskRepository.save(existingTask);
    }

    /**
     * deletes a task from task ID
     * @param id ID to delete the task
     * @return true if deletion is a success else false
     */
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    //  create a new history object and save it
                    TaskHistory history = new TaskHistory(task,"Task Deleted",LocalDateTime.now());
                    taskHistoryRepository.save(history);

                    // successfully delete the task
                    taskRepository.delete(task);
                    return true;
                }).orElseThrow(() -> new NoSuchElementException("Task not found for ID: "+id));
    }

    /**
     * gets a list of tasks on a history basis
     * @param id task id to get the history
     * @return the pageable of history for a particular task
     */
    public List<TaskHistory> getTaskHistory(Long id) {
        List<TaskHistory> taskHistoryList = taskHistoryRepository.findTaskHistoriesByTaskId(id);
        if (taskHistoryList.isEmpty() || taskHistoryList == null){
            throw new NoSuchElementException("No task history found for ID: "+id);
        }
        return taskHistoryList;
    }

}
