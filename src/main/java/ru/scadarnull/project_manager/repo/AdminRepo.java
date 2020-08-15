package ru.scadarnull.project_manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.User;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<User, Long> {

    List<User> findAll();

    User findByName(String name);

}
