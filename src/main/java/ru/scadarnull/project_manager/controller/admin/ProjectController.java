package ru.scadarnull.project_manager.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserProject;
import ru.scadarnull.project_manager.exceptions.IsExistException;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.ProjectService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/project")
    public List<Project> list(){
        return projectService.getAll();
    }

    @GetMapping("/project/{id}")
    public Project getOne(@PathVariable("id") Project project){
        if(project == null){
            throw new NotFoundException("Проект не найден");
        }
        return project;
    }

    @PostMapping("/project")
    public Project create(@RequestBody @Valid Project project, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new NotValidException("Невалидные данные");
        }
        if(!projectService.addProject(project)){
            throw new IsExistException("Такой проект уже есть");
        }
        return project;
    }

    @PutMapping("/project/{id}")
    public Project update(@PathVariable("id") Project projectFromDB, @RequestBody Project project){
        if(projectService.projectIsExist(project) && !projectFromDB.getName().equals(project.getName())){
            throw new IsExistException("Такой проект уже есть");
        }
        if(project.getName() != null){
            projectFromDB.setName(project.getName());
        }
        if(project.getStartTime() != null){
            projectFromDB.setStartTime(project.getStartTime());
        }
        projectService.updateProject(projectFromDB);
        return projectFromDB;
    }

    @GetMapping("/project/{id}/users")
    public List<User> getUsersInProject(@PathVariable("id") Project project){
        if(project == null){
            throw new NotFoundException("Проект не найден");
        }
        return projectService.getUsersByProject(project);
    }

    @PostMapping("/project/{projectId}/user/{userId}")
    public UserProject getUserInProject(@PathVariable("projectId") Project project,
                                         @PathVariable("userId") User user,
                                         @RequestBody Map<String, String> params){
        if(project == null){
            throw new NotFoundException("Проект не найден");
        }
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        if(params.get("teamRole") == null){
            throw new NotValidException("Надо добавить teamRole");
        }
        if(params.get("isActive") == null){
            throw new NotValidException("Надо добавить isActive");
        }
        return projectService.addUserInProject(user, project, params);
    }

    @PutMapping("/project/{projectId}/user/{userId}")
    public UserProject updateUserInProject(@PathVariable("projectId") Project project,
                                         @PathVariable("userId") User user,
                                         @RequestBody Map<String, String> params){
        if(project == null){
            throw new NotFoundException("Проект не найден");
        }
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        return projectService.updateUserInProject(user, project, params);
    }

    @DeleteMapping("/project/{id}")
    public void delete(@PathVariable("id") Project project){
        projectService.deleteProject(project);
    }
}
