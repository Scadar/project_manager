package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Role;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.repo.ProjectRepo;

import java.util.Collections;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    public List<Project> findAllProjects() {
        return projectRepo.findAll();
    }

    public boolean addProject(Project project) {
        if(projectIsExist(project)){
            return false;
        }
        projectRepo.save(project);
        return true;
    }

    public boolean projectIsExist(Project project){
        Project projectFromDb = projectRepo.findByName(project.getName());
        return projectFromDb != null;
    }

    public boolean projectIsExist(String project){
        Project projectFromDb = projectRepo.findByName(project);
        return projectFromDb != null;
    }

    public void updateProject(Project project) {
        projectRepo.save(project);
    }

    public void deleteProject(Project project) {
        projectRepo.delete(project);
    }

    public Project findByName(String project) {
        return projectRepo.findByName(project);
    }
}
