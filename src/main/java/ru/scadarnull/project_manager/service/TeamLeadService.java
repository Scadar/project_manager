package ru.scadarnull.project_manager.service;

import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.ForbiddenException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.repo.*;

import java.util.List;
import java.util.Map;

@Service
public class TeamLeadService {

    private final UserRepo userRepo;
    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private final UserProjectRepo userProjectRepo;
    private final UserTaskRepo userTaskRepo;

    public TeamLeadService(UserRepo userRepo,
                           TaskRepo taskRepo,
                           ProjectRepo projectRepo,
                           UserProjectRepo userProjectRepo,
                           UserTaskRepo userTaskRepo)
    {
        this.userRepo = userRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.userProjectRepo = userProjectRepo;
        this.userTaskRepo = userTaskRepo;
    }

    public UserTask addRelationship(User authUser, Map<String, String> param) {
        if(param.get("user") == null){
            throw new NotValidException("Нет параметра user");
        }
        if(param.get("task") == null){
            throw new NotValidException("Нет параметра task");
        }
        User userFromParameter = userRepo.findByName(param.get("user"));
        Task task = taskRepo.findByName(param.get("task"));
        Project project = task.getProject();
        List<Project> teamLeadProjects = projectRepo.findProjectsByUser(authUser);
        List<Project> userProjects = projectRepo.findProjectsByUser(userFromParameter);

        if(teamLeadProjects.contains(project) && userProjects.contains(project)){
            UserProject up = getUserProjectByUserAndProject(authUser, project);
            if(up != null){
                if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                    UserTask userTask = new UserTask();
                    userTask.setTime(0);
                    userTask.setTask(task);
                    userTask.setUser(userFromParameter);
                    userTaskRepo.save(userTask);
                    return userTask;
                }
                throw new ForbiddenException("Вы не тимлид");
            }
            throw new ForbiddenException("Вас нет в данном проекте");
        }
        throw new ForbiddenException("Либо тимлида нет в проекте, либо юзера, а мб и обоих");
    }

    private UserProject getUserProjectByUserAndProject(User user, Project project){
        for(UserProject up : userProjectRepo.findUserProjectByUser(user)){
            if(up.getProject().equals(project)){
                return up;
            }
        }
        return null;
    }

    public Task addTask(User user, Task task, String projectName) {
        Project project = projectRepo.findByName(projectName);
        List<Project> userProjects = projectRepo.findProjectsByUser(user);
        if(userProjects.contains(project)){
            UserProject up = getUserProjectByUserAndProject(user, project);
            if(up != null){
                if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                    task.setProject(project);
                    taskRepo.save(task);
                    return task;
                }
                throw new ForbiddenException("Вы не тимлид");
            }
        }
        throw new ForbiddenException("Не ваш проект");
    }

    public Task updateTask(User user, Map<String, String> param) {
        if(param.get("task") == null){
            throw new NotValidException("Нет параметра task");
        }
        Task task = taskRepo.findByName(param.get("task"));
        Project project = task.getProject();
        List<Project> userProjects = projectRepo.findProjectsByUser(user);
        if(userProjects.contains(project)){
            UserProject up = getUserProjectByUserAndProject(user, project);
            if(up != null){
                if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                    if(param.get("state") != null){
                        task.setState(State.valueOf(param.get("state")));
                    }
                    if(param.get("name") != null){
                        task.setName(param.get("name"));
                    }
                    if(param.get("description") != null){
                        task.setDescription(param.get("description"));
                    }
                    taskRepo.save(task);
                    return task;
                }
                throw new ForbiddenException("Вы не тимлид");
            }
        }
        throw new ForbiddenException("Это не ваш проект");
    }

    public void deleteTask(User user, Map<String, String> param) {
        if(param.get("task") == null){
            throw new NotValidException("Нет параметра task");
        }
        Task task = taskRepo.findByName(param.get("task"));
        Project project = task.getProject();
        List<Project> userProjects = projectRepo.findProjectsByUser(user);
        if(userProjects.contains(project)){
            UserProject up = getUserProjectByUserAndProject(user, project);
            if(up != null){
                if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                    taskRepo.delete(task);
                }
                throw new ForbiddenException("Вы не тимлид");
            }
        }
        throw new ForbiddenException("Это не ваш проект");
    }
}
