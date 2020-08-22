package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserProject;
import ru.scadarnull.project_manager.entity.UserTask;

import java.util.List;

@Repository
public interface UserTaskRepo extends JpaRepository<UserTask, Long> {

    @Query("SELECT ut FROM UserTask ut WHERE ut.user = :user")
    List<UserTask> findUserTaskByUser(@Param("user") User user);
}
