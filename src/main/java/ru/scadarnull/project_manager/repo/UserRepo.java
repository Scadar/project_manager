package ru.scadarnull.project_manager.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.scadarnull.project_manager.entity.User;

import java.util.List;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
    List<User> findAll();
    User findByName(String name);
}
