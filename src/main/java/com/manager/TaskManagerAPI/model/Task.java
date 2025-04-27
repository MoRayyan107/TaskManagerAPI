package com.manager.TaskManagerAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity // creates a table into a local database (H2)
public class Task {

    @Id // marking this a primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generates ID
    private Long id;

    @NotBlank(message = "Title cannot be Empty")
    @Size(max = 100, message = "title must be less than 100 characters!")
    private String title;

    @NotNull(message = "Description cannot be empty!")
    private String description;
    private boolean completed;

    // constructor
    public Task() {}
    public Task(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }
    // getters
    public Long getID() {return id;}
    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public boolean isCompleted() {return completed;}

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                '}';
    }

    //setters
    public void setID(Long id) {this.id = id;} // test usage
    public void setTitle(String Name) {title=Name;}
    public void setDescription(String Description) {description=Description;}
    public void setCompleted(boolean Completed) {completed=Completed;}

}

