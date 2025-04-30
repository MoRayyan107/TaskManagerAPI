package com.manager.TaskManagerAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity // creates a table into a local database (H2)
public class Task {

    @Id // marking this a primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generates ID
    private Long id;

    @NotNull(message = "Title cannot be Empty")
    @Size(max = 100, message = "title must be less than 100 characters!")
    private String title;

    @NotNull(message = "Description cannot be empty!")
    private String description;
    private boolean completed;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    // constructor
    public Task() {}
    public Task(String title, String description, boolean completed, Priority priority) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.priority = priority;
    }
    public enum Priority{
        HIGH, MEDIUM, LOW
    }
    // getters
    public Long getId() {return id;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public boolean isCompleted() {return completed;}
    public Priority getPriority() {return priority;}

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id + "," +
                "\"title\":\"" + title + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"completed\":" + completed +
                "}";
    }

    //setters
    public void setId(Long id) {this.id = id;} // test usage
    public void setTitle(String Name) {title=Name;}
    public void setDescription(String Description) {description=Description;}
    public void setCompleted(boolean Completed) {completed=Completed;}
    public void setPriority(Priority Priority) {priority=Priority;}

}

