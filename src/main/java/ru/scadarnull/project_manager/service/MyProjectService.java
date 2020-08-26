package ru.scadarnull.project_manager.service;

import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.ForbiddenException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.TaskRepo;
import ru.scadarnull.project_manager.repo.UserTaskRepo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyProjectService {

    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;
    private final UserTaskRepo userTaskRepo;

    public MyProjectService(ProjectRepo projectRepo, TaskRepo taskRepo, UserTaskRepo userTaskRepo) {
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
        this.userTaskRepo = userTaskRepo;
    }

    public List<Project> getMyProjects(User user){
        return projectRepo.findProjectsByUser(user);
    }

    public List<Task> getMyTasks(User user) {
        return taskRepo.findTaskByUser(user);
    }

    public List<Task> getMyTasks(User user, String filter) {
        List<Task> taskByUser = getMyTasks(user);
        return taskByUser.stream().filter(task -> task.getName().contains(filter)).collect(Collectors.toList());
    }

    public Task updateTask(User user, Map<String, String> param) {
        if(param.get("task") == null){
            throw new NotValidException("Нет параметра task");
        }
        Task task = taskRepo.findByName(param.get("task"));
        UserTask userTask = getUserTaskByUserAndTask(user, task);
        if(userTask != null){
            if(!task.getState().equals(State.UNDER_CONSIDERATION)){
                task.setState(State.valueOf(param.get("state")));
                taskRepo.save(task);
                return task;
            }else{
                throw new ForbiddenException("Вы не можете менять это состояние");
            }
        }
        throw new ForbiddenException("Это не ваша задача");
    }

    public UserTask updateTime(User user, Map<String, String> param) {
        if(param.get("task") == null){
            throw new NotValidException("Нет параметра task");
        }
        UserTask userTask = getUserTaskByUserAndTask(user, taskRepo.findByName(param.get("task")));
        if(userTask != null){
            userTask.setTime(userTask.getTime() + Integer.parseInt(param.get("time")));
            userTaskRepo.save(userTask);
            return userTask;
        }
        throw new ForbiddenException("Это не ваша задача");
    }

    private UserTask getUserTaskByUserAndTask(User user, Task task){
        for(UserTask userTask : userTaskRepo.findUserTaskByUser(user)){
            if(userTask.getTask().equals(task)){
                return userTask;
            }
        }
        return null;
    }
}
