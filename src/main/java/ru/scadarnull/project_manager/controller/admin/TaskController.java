package ru.scadarnull.project_manager.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.ProjectService;
import ru.scadarnull.project_manager.service.TaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;

    @Autowired
    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping("/task")
    public List<Task> tasks(){
        return taskService.getAll();
    }

    @GetMapping("/task/{id}")
    public Task getOne(@PathVariable("id") Task task){
        if(task == null){
            throw new NotFoundException("Задача не найдена");
        }
        return task;
    }

    @PostMapping("/task/project/{projectId}")
    public Task create(@AuthenticationPrincipal User user,
                       @RequestBody @Valid Task task,
                       @PathVariable("projectId") Project project,
                       BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new NotValidException("Невалидные данные");
        }
        if(project == null){
            throw new NotFoundException("Проект не найден");
        }
        return taskService.addTask(task, project, user);
    }

    @PutMapping("/task/{id}")
    public Task update(@PathVariable("id") Task taskFromDB,
                       @RequestBody Task task
    ){
        if(task.getName() != null){
            taskFromDB.setName(task.getName());
        }
        if(task.getDescription() != null){
            taskFromDB.setDescription(task.getDescription());
        }
        if(task.getActualEndTime() != null){
            taskFromDB.setActualEndTime(task.getActualEndTime());
        }
        if(task.getState() != null){
            taskFromDB.setState(task.getState());
        }
        taskService.updateTask(taskFromDB);
        return taskFromDB;
    }

    @GetMapping("/task/{id}/users")
    public List<User> getUsersByTask(@PathVariable("id") Task task){
        if(task == null){
            throw new NotFoundException("Task не найден");
        }
        return taskService.getUsersByTask(task);
    }

    @PostMapping("/task/{taskId}/user/{userId}")
    public UserTask addUserInTask(   @PathVariable("taskId") Task task,
                                     @PathVariable("userId") User user){
        if(task == null){
            throw new NotFoundException("Task не найден");
        }
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        return taskService.addUserInTask(user, task);
    }

    @PutMapping("/task/{taskId}/user/{userId}")
    public UserTask updateUserInProject(@PathVariable("taskId") Task task,
                                           @PathVariable("userId") User user,
                                           @RequestBody Map<String, String> params){
        if(task == null){
            throw new NotFoundException("Проект не найден");
        }
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        if(params.get("time") == null){
            throw new NotValidException("Нет поля time");
        }
        return taskService.updateUserInTask(user, task, params);
    }

    @DeleteMapping("/task/{id}")
    public void delete(@PathVariable("id") Task task){
        taskService.deleteTask(task);
    }
}