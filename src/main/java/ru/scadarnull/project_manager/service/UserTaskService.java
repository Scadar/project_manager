package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserTask;
import ru.scadarnull.project_manager.repo.UserTaskRepo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class UserTaskService {
    private UserTaskRepo userTaskRepo;
    private AdminService adminService;
    private TaskService taskService;

    @Autowired
    public UserTaskService(UserTaskRepo userTaskRepo, AdminService adminService, TaskService taskService) {
        this.userTaskRepo = userTaskRepo;
        this.adminService = adminService;
        this.taskService = taskService;
    }

    public UserTask add(Map<String, String> userTaskAsMap){
        User user = adminService.findByName(userTaskAsMap.get("user"));
        Task task = taskService.findByName(userTaskAsMap.get("task"));
        Integer time = Integer.valueOf(userTaskAsMap.get("time"));
        UserTask userTask = new UserTask();
        userTask.setUser(user);
        userTask.setTask(task);
        userTask.setTime(time);
        userTaskRepo.save(userTask);
        return userTask;
    }

    public List<UserTask> findAll() {
        return userTaskRepo.findAll();
    }

    public boolean userIsExist(String name) {
        return adminService.userIsExist(name);
    }

    public boolean taskIsExist(String task) {
        return taskService.taskIsExist(task);
    }

    public void delete(UserTask userTask) {
        userTaskRepo.delete(userTask);
    }

    public void updateTime(UserTask userTask, Integer time) {
        userTask.setTime(userTask.getTime() + time);
        userTaskRepo.save(userTask);
    }
}
