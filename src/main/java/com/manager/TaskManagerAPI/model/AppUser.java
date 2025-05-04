package com.manager.TaskManagerAPI.model;

import jakarta.persistence.*;

import static com.manager.TaskManagerAPI.constants.AppConstants.ROLE_USER;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String Role = ROLE_USER;
    private String firstName;
    private String lastName;
    private String email;

    // getters
    public Long getId() {return id;}
    public String getUsername() {return username;}
    public String getRole() {return Role;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}

    // setters
    public void setId(Long id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(String Role) {this.Role = Role;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEmail(String email) {this.email = email;}
}
