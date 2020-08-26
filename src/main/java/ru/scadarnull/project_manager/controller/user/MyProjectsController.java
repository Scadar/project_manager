package ru.scadarnull.project_manager.controller.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserTask;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.ProjectService;
import ru.scadarnull.project_manager.service.TaskService;
import ru.scadarnull.project_manager.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class MyProjectsController {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final UserService userService;

    public MyProjectsController(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/my_project/project")
    public List<Project> getMyProjects(@AuthenticationPrincipal User user){
        return projectService.getAll(user);
    }

    @GetMapping("/my_project/task")
    public List<Task> getMyTasks(@AuthenticationPrincipal User user){
        return taskService.getAll(user);
    }

    @GetMapping("/my_project/project/{projectId}/task")
    public List<Task> getMyTasksInProject(@AuthenticationPrincipal User user,
                                          @PathVariable("projectId") Project project){
        if(project == null){
            throw new NotFoundException("Такого проекта нет");
        }
        return userService.getTaskByUserAndProject(user, project);
    }

    @PostMapping("/my_project/task/project/{projectId}")
    public Task addTask(@AuthenticationPrincipal User user,
                        @RequestBody @Valid Task task,
                        @PathVariable("projectId") Project project,
                        BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            throw new NotValidException("Невалидные данные");
        }
        if(project == null){
            throw new NotFoundException("Проект не найдена");
        }
        return taskService.addTask(task, project, user);
    }

    @PutMapping("/my_project/task/{taskId}/time")
    public UserTask updateTimeInTask(   @AuthenticationPrincipal User user,
                                        @PathVariable("taskId") Task task,
                                        @RequestBody Map<String, String> param){
        if(task == null){
            throw new NotFoundException("Task not found");
        }
        return taskService.updateTimeInTask(user,task, param);
    }

    @PutMapping("/my_project/task/{taskId}/state")
    public Task updateStateInTask(  @AuthenticationPrincipal User user,
                                    @PathVariable("taskId") Task task,
                                    @RequestBody Map<String, String> param){
        if(task == null){
            throw new NotFoundException("Task not found");
        }
        if(param.get("state") == null){
            throw new NotValidException("Нет поля state");
        }
        return taskService.updateStateInTask(user,task, param);
    }
}
