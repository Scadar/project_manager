package ru.scadarnull.project_manager.service;

import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.ForbiddenException;
import ru.scadarnull.project_manager.repo.*;

import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private final UserProjectRepo userProjectRepo;
    private final UserRepo userRepo;
    private final UserTaskRepo userTaskRepo;

    public TaskService(TaskRepo taskRepo, ProjectRepo projectRepo, UserProjectRepo userProjectRepo, UserRepo userRepo, UserTaskRepo userTaskRepo) {
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.userProjectRepo = userProjectRepo;
        this.userRepo = userRepo;
        this.userTaskRepo = userTaskRepo;
    }

    public List<Task> getAll() {
        return taskRepo.findAll();
    }

    public Task addTask(Task task, Project project, User user){
        if(!user.getRoles().contains(Role.ADMIN)){
            if(!projectRepo.findProjectsByUser(user).contains(project)){
                throw new ForbiddenException("Это не ваш проект");
            }
            replaceStateIfNotTeamLead(task, project, user);
        }
        task.setProject(project);
        taskRepo.save(task);
        return task;
    }

    private void replaceStateIfNotTeamLead(Task task, Project project, User user) {
        List<UserProject> userProjects = userProjectRepo.findUserProjectByUser(user);
        for(UserProject up : userProjects){
            if(up.getProject().equals(project)){
                if(!up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                    task.setState(State.UNDER_CONSIDERATION);
                    break;
                }
            }
        }
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

    public boolean taskIsExist(Task task) {
        return taskIsExist(task.getName());
    }

    public boolean taskIsExist(String task) {
        Task taskFromDb = findByName(task);
        return taskFromDb != null;
    }

    public List<User> getUsersByTask(Task task) {
        return userRepo.getUsersByTask(task);
    }

    public UserTask addUserInTask(User user, Task task) {
        Project project = task.getProject();
        List<Project> userProjects = projectRepo.findProjectsByUser(user);

        if(!userProjects.contains(project)){
            throw new ForbiddenException("Данный пользователь не состоит в проекте данного таска");
        }

        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setTime(0);
        userTaskRepo.save(userTask);
        return userTask;
    }

    public UserTask updateUserInTask(User user, Task task, Map<String, String> params) {
        UserTask userTask = userTaskRepo.findByUserAndTask(user, task);
        userTask.setTime(Integer.parseInt(params.get("time")));
        userTaskRepo.save(userTask);
        return userTask;
    }
}
