package com.manager.TaskManagerAPI.repository;

import com.manager.TaskManagerAPI.model.TaskHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

    // geta the list of tasks history paged
    List<TaskHistory> findTaskHistoriesByTaskId(Long tasId);
}
