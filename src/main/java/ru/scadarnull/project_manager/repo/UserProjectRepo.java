package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.UserProject;

@Repository
public interface UserProjectRepo extends JpaRepository<UserProject, Long> {
}
