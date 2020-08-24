package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserProject;

import java.util.List;

@Repository
public interface UserProjectRepo extends JpaRepository<UserProject, Long> {

    @Query("SELECT up FROM UserProject up WHERE up.user = :user")
    List<UserProject> findUserProjectByUser(@Param("user") User user);

}
