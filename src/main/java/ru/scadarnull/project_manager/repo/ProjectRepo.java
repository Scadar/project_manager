package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.User;

import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
    Project findByName(String name);

    @Query("SELECT up.project FROM UserProject up WHERE up.user = :user")
    List<Project> findProjectByUser(@Param("user") User user);
}
