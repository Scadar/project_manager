package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.TaskRepo;
import ru.scadarnull.project_manager.repo.UserProjectRepo;

import java.util.List;

@Service
public class TaskService {
    private TaskRepo taskRepo;
    private ProjectRepo projectRepo;
    private UserProjectRepo userProjectRepo;

    public TaskService(TaskRepo taskRepo, ProjectRepo projectRepo, UserProjectRepo userProjectRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.userProjectRepo = userProjectRepo;
    }

    public List<Task> getAll() {
        return taskRepo.findAll();
    }

    public boolean addTask(Task task, String project, User user){
        if(taskIsExist(task)){
            return false;
        }
        Project projectFromDB = projectRepo.findByName(project);
        if(!user.getRoles().contains(Role.ADMIN)){
            if(!projectRepo.findProjectByUser(user).contains(projectFromDB)){
                return false;
            }
            List<UserProject> userProjects = userProjectRepo.findProjectByUser(user);
            for(UserProject up : userProjects){
                if(up.getProject().equals(projectFromDB)){
                    if(!up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                        task.setState(State.UNDER_CONSIDERATION);
                        break;
                    }
                }
            }
        }
        task.setProject(projectFromDB);
        taskRepo.save(task);
        return true;
    }

    public boolean taskIsExist(Task task) {
        Task taskFromDb = taskRepo.findByName(task.getName());
        return taskFromDb != null;
    }

    public void deleteTask(Task task) {
        taskRepo.delete(task);
    }

    public void updateTask(Task task) {
        taskRepo.save(task);
    }

    public Task findByName(String name) {
        return taskRepo.findByName(name);
    }

    public boolean taskIsExist(String task) {
        Task taskFromDb = taskRepo.findByName(task);
        return taskFromDb != null;
    }
}
