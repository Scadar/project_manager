package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Task findByName(String name);

    @Query("SELECT ut.task FROM UserTask ut WHERE ut.user = :user")
    List<Task> findTaskByUser(@Param("user") User user);
}
