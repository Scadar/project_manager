package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.*;

import java.util.List;

@Repository
public interface UserTaskRepo extends JpaRepository<UserTask, Long> {

    @Query("SELECT ut FROM UserTask ut WHERE ut.user = :user")
    List<UserTask> findUserTaskByUser(@Param("user") User user);

    @Query("SELECT ut FROM UserTask ut WHERE ut.task = :task")
    List<UserTask> findUserTaskByTask(@Param("task") Task task);

    @Query("SELECT ut FROM UserTask ut WHERE ut.user = :user and ut.task = :task")
    UserTask findByUserAndTask(@Param("user") User user, @Param("task") Task task);

}
