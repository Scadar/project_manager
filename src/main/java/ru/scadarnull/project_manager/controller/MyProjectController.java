package ru.scadarnull.project_manager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserTask;
import ru.scadarnull.project_manager.exceptions.IsExistException;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.MyProjectService;
import ru.scadarnull.project_manager.service.TaskService;
import ru.scadarnull.project_manager.utils.DBUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class MyProjectController {
    private final MyProjectService myProjectService;
    private final TaskService taskService;
    private final DBUtils dbUtils;

    public MyProjectController(MyProjectService myProjectService, TaskService taskService, DBUtils dbUtils) {
        this.myProjectService = myProjectService;
        this.taskService = taskService;
        this.dbUtils = dbUtils;
    }

    @GetMapping("/my_project")
    public List<Project> getMyProjects(@AuthenticationPrincipal User user){
        return myProjectService.getMyProjects(user);
    }

    @GetMapping("/my_project/task")
    public List<Task> getMyTasks(@AuthenticationPrincipal User user, @RequestParam(required = false) String filter){
        if(filter != null){
            return myProjectService.getMyTasks(user, filter);
        }
        return myProjectService.getMyTasks(user);
    }

    @PostMapping("/my_project/task")
    public Task addTask(@AuthenticationPrincipal User user,
                        @RequestBody @Valid Task task,
                        @RequestParam String project,
                        BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            throw new NotValidException("Невалидные данные");
        }
        if(!dbUtils.projectIsExist(project)){
            throw new NotFoundException("Проект не найдена");
        }
        if(!taskService.addTask(task, project, user)){
            throw new IsExistException("Такой проект уже есть / Нет доступа");
        }
        return task;
    }

    @PutMapping("/my_project/task")
    public Task updateTask(@AuthenticationPrincipal User user,
                           @RequestBody Map<String, String> param){
        if(!dbUtils.taskIsExist(param.get("task"))){
            throw new NotFoundException("Task не найден");
        }
        return myProjectService.updateTask(user, param);
    }

    @PutMapping("/my_project/user_task")
    public UserTask updateTime(@AuthenticationPrincipal User user,
                               @RequestBody Map<String, String> param){
        if(!dbUtils.taskIsExist(param.get("task"))){
            throw new NotFoundException("Task не найден");
        }
        return myProjectService.updateTime(user, param);
    }
}
