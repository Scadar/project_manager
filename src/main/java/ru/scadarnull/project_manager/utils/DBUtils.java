package ru.scadarnull.project_manager.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.repo.AdminRepo;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.TaskRepo;
import ru.scadarnull.project_manager.repo.UserTaskRepo;
import ru.scadarnull.project_manager.service.TaskService;
import ru.scadarnull.project_manager.service.UserTaskService;

@Service
public class DBUtils {
    private AdminRepo adminRepo;
    private TaskRepo taskRepo;
    private ProjectRepo projectRepo;

    @Autowired
    public DBUtils(AdminRepo adminRepo, TaskRepo taskRepo, ProjectRepo projectRepo) {
        this.adminRepo = adminRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
    }

    public boolean userIsExist(String user){
        User userFromDb = adminRepo.findByName(user);
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
