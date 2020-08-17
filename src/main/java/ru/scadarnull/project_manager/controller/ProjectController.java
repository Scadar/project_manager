package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.service.ProjectService;

import java.util.List;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/project")
    public List<Project> list(){
        return projectService.findAllProjects();
    }

    @GetMapping("/project/{id}")
    public Project getOne(@PathVariable("id") Project project){
        if(project == null){
            throw new NotFoundException();
        }
        return project;
    }

    @PostMapping("/project")
    public Project create(@RequestBody Project project){
        if(!projectService.addProject(project)){
            throw new NotFoundException();
        }
        return project;
    }

    @PutMapping("/project/{id}")
    public Project update(@PathVariable("id") Project projectFromDB, @RequestBody Project project){
        if(projectService.projectIsExist(project)){
            throw new NotFoundException();
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

    @DeleteMapping("/project/{id}")
    public void delete(@PathVariable("id") Project project){
        projectService.deleteProject(project);
    }
}
