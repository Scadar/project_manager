package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    Task findByName(String name);
}
