package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.exceptions.IsExistException;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.ProjectService;
import ru.scadarnull.project_manager.service.TaskService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TaskController {

    private TaskService taskService;
    private ProjectService projectService;

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

    @PostMapping("/task")
    public Task create(@RequestBody @Valid Task task, @RequestParam(required = true) String project, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new NotValidException("Невалидные данные");
        }
        if(!projectService.projectIsExist(project)){
            throw new NotFoundException("Проект не найдена");
        }
        if(!taskService.addTask(task, project)){
            throw new IsExistException("Такой проект уже есть");
        }
        return task;
    }

    @PutMapping("/task/{id}")
    public Task update(@PathVariable("id") Task taskFromDB,
                       @RequestBody Task task,
                       @RequestParam(required = false) String project
    ){
        if(taskService.taskIsExist(task) && !taskFromDB.getName().equals(task.getName())){
            throw new IsExistException("Такая задача уже есть");
        }
        if(project!=null && !projectService.projectIsExist(project)){
            throw new NotFoundException("Проект не найдена");
        }else if(project != null){
           taskFromDB.setProject(projectService.findByName(project));
        }
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

    @DeleteMapping("/task/{id}")
    public void delete(@PathVariable("id") Task task){
        taskService.deleteTask(task);
    }
}
