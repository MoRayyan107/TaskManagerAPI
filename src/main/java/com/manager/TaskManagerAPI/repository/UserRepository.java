package com.manager.TaskManagerAPI.repository;

import com.manager.TaskManagerAPI.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Integer> {

    // gets the user by its username Optional format for not causing crashes
    Optional<AppUser> findByUsername(String username);
}
