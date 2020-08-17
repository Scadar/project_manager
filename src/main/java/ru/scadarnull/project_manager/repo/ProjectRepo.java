package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.Project;

@Repository
public interface ProjectRepo extends JpaRepository<Project, Long> {
    Project findByName(String name);
}
