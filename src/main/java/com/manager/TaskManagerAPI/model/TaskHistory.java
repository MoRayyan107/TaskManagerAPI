package com.manager.TaskManagerAPI.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;  // many task history can be stored for a task

    private String changedAction;  // what changed when updated
    private LocalDateTime time; // at what time it changed

    public TaskHistory() {}
    public TaskHistory(Task task, String changedAction, LocalDateTime time) {
        this.task = task;
        this.changedAction = changedAction;
        this.time = time;
    }

    // getters
    public Task getTask() {return task;}
    public String getChangedAction() {return changedAction;}
    public LocalDateTime getTime() {return time;}

    // setters
    public void setTask(Task task) {this.task = task;}
    public void setChangedAction(String changedAction) {this.changedAction = changedAction;}
    public void setTime(LocalDateTime time) {this.time = time;}

}
