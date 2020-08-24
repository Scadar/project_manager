package ru.scadarnull.project_manager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.TaskRepo;
import ru.scadarnull.project_manager.service.UserService;

@Service
public class DBUtils {
    private UserService userService;
    private TaskRepo taskRepo;
    private ProjectRepo projectRepo;

    @Autowired
    public DBUtils(UserService userService, TaskRepo taskRepo, ProjectRepo projectRepo) {
        this.userService = userService;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
    }

    public boolean userIsExist(String user){
        User userFromDb = userService.findByName(user);
        return userFromDb != null;
    }

    public boolean taskIsExist(String task) {
        Task taskFromDb = taskRepo.findByName(task);
        return taskFromDb != null;
    }

    public boolean projectIsExist(String project) {
        Project projectFromDb = projectRepo.findByName(project);
        return projectFromDb != null;
    }
}
