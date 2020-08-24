package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.UserTask;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.service.UserTaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class UserTaskController {
    private UserTaskService userTaskService;

    @Autowired
    public UserTaskController(UserTaskService userTaskService) {
        this.userTaskService = userTaskService;
    }

    @GetMapping("/user_task")
    public List<UserTask> showAll(){
        return userTaskService.findAll();
    }

    @GetMapping("/user_task/{id}")
    public UserTask getOne(@PathVariable("id") UserTask userTask){
        if(userTask == null){
            throw new NotFoundException("User_Task не найден");
        }
        return userTask;
    }

    @PostMapping("/user_task")
    public UserTask create(@RequestBody  Map<String, String> param){
        if(!userTaskService.userIsExist(param.get("user"))){
            throw new NotFoundException("Пользователь не найден");
        }
        if(!userTaskService.taskIsExist(param.get("task"))){
            throw new NotFoundException("Task не найден");
        }
        UserTask userTask = userTaskService.add(param);
        return userTask;
    }

    @PutMapping("/user_task/{id}")
    public UserTask update(@PathVariable("id") UserTask userTaskFromDB,
                       @RequestBody Map<String, String> param) {
        userTaskService.updateTime(userTaskFromDB, Integer.valueOf(param.get("time")));
        return userTaskFromDB;
    }

    @DeleteMapping("/user_task/{id}")
    public void delete(@PathVariable("id") UserTask userTask){
        userTaskService.delete(userTask);
    }
}
