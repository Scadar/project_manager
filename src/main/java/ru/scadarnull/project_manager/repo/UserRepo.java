package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    List<User> findAll();
    User findByName(String name);

    @Query("SELECT up.user FROM UserProject up WHERE up.project = :project")
    List<User> getUsersByProject(@Param("project")Project project);

    @Query("SELECT ut.user FROM UserTask ut WHERE ut.task = :task")
    List<User> getUsersByTask(@Param("task") Task task);
}
