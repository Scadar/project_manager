package ru.scadarnull.project_manager.service;

import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.ForbiddenException;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
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

    public List<Task> getAll(User user) {
        return taskRepo.findTasksByUser(user);
    }

    public Task addTask(Task task, Project project){
        task.setProject(project);
        taskRepo.save(task);
        return task;
    }

    public Task addTask(Task task, Project project, User user){
        if(!user.getRoles().contains(Role.ADMIN)){
            if(!projectRepo.findProjectsByUser(user).contains(project)){
                throw new ForbiddenException("Это не ваш проект");
            }
            replaceStateIfNotTeamLead(task, project, user);
        }
        return addTask(task, project);
    }

    private void replaceStateIfNotTeamLead(Task task, Project project, User user) {
        UserProject userProject = userProjectRepo.findByUserAndProject(user, project);
        if(!userProject.getTeamRole().equals(TeamRole.TEAM_LEAD)){
            task.setState(State.UNDER_CONSIDERATION);
        }
    }

    public void deleteTask(Task task) {
        taskRepo.delete(task);
    }

    public Task updateTask(Task taskFromDB, Task taskFromRequest) {
        if(taskFromRequest.getName() != null){
            taskFromDB.setName(taskFromRequest.getName());
        }
        if(taskFromRequest.getDescription() != null){
            taskFromDB.setDescription(taskFromRequest.getDescription());
        }
        if(taskFromRequest.getActualEndTime() != null){
            taskFromDB.setActualEndTime(taskFromRequest.getActualEndTime());
        }
        if(taskFromRequest.getState() != null){
            taskFromDB.setState(taskFromRequest.getState());
        }
        return taskRepo.save(taskFromDB);
    }

    public Task updateTask(Task taskFromDB, Task taskFromRequest, User user) {
        UserProject userProject = userProjectRepo.findByUserAndProject(user, taskFromDB.getProject());
        if(userProject == null){
            throw new ForbiddenException("Это не ваш проект");
        }
        if(!userProject.getTeamRole().equals(TeamRole.TEAM_LEAD)){
            throw new ForbiddenException("Вы не тимлид в этом пректе");
        }
        return updateTask(taskFromDB, taskFromRequest);
    }

    public List<User> getUsersByTask(Task task) {
        return userRepo.getUsersByTask(task);
    }

    public UserTask addUserInTask(User user, Task task) {
        List<Project> userProjects = projectRepo.findProjectsByUser(user);
        if(!userProjects.contains(task.getProject())){
            throw new ForbiddenException("Данный пользователь не состоит в проекте данного таска");
        }
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setTime(0);
        userTaskRepo.save(userTask);
        return userTask;
    }


    public UserTask addUserInTask(User currentUser, User user, Task task) {
        if(!currentUser.getRoles().contains(Role.ADMIN)){
            UserProject up = userProjectRepo.findByUserAndProject(currentUser, task.getProject());
            if(up == null || !up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                throw new ForbiddenException("Вы не тимлид");
            }
        }
        return addUserInTask(user, task);
    }

    public UserTask updateTimeInTask(User user, Task task, Map<String, String> params) {
        if(!user.getRoles().contains(Role.ADMIN)){
            if(!taskRepo.findTasksByUser(user).contains(task)){
                throw new ForbiddenException("Это не ваш Таск");
            }
        }
        UserTask userTask = userTaskRepo.findByUserAndTask(user, task);
        userTask.setTime(Integer.parseInt(params.get("time")));
        userTaskRepo.save(userTask);
        return userTask;
    }

    public Task updateStateInTask(User user, Task task, Map<String, String> param){
        if(!taskRepo.findTasksByUser(user).contains(task)){
            throw new ForbiddenException("Это не ваш Таск");
        }
        if(task.getState().equals(State.UNDER_CONSIDERATION)){
            throw new ForbiddenException("Вы не можете изменить это состояние");
        }
        if(State.valueOf(param.get("state")).equals(State.UNDER_CONSIDERATION)){
            throw new ForbiddenException("Вы не можете изменить на это состояние");
        }
        task.setState(State.valueOf(param.get("state")));
        return taskRepo.save(task);
    }

    public Integer timesheet(Task task) {
        return userTaskRepo.findUserTaskByTask(task).stream().mapToInt(UserTask::getTime).sum();
    }
}
