package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.repo.UserProjectRepo;

import java.util.List;
import java.util.Map;

@Service
public class UserProjectService {

    private UserProjectRepo userProjectRepo;
    private UserService userService;
    private ProjectService projectService;

    @Autowired
    public UserProjectService(UserProjectRepo userProjectRepo,
                              UserService userService,
                              ProjectService projectService)
    {
        this.userProjectRepo = userProjectRepo;
        this.userService = userService;
        this.projectService = projectService;
    }

    public List<UserProject> findAll() {
        return userProjectRepo.findAll();
    }

    public UserProject add(Map<String, String> param) {
        if(param.get("user") == null){
            throw new NotValidException("Нет поля user");
        }
        if(param.get("project") == null){
            throw new NotValidException("Нет поля project");
        }
        if(param.get("is_active") == null){
            throw new NotValidException("Нет поля is_active");
        }
        if(param.get("role") == null){
            throw new NotValidException("Нет поля role");
        }
        UserProject userProject = new UserProject();
        userProject.setUser(userService.findByName(param.get("user")));
        userProject.setProject(projectService.findByName(param.get("project")));
        userProject.setIsActive(Boolean.valueOf(param.get("is_active")));
        userProject.setTeamRole(TeamRole.valueOf(param.get("role")));
        userProjectRepo.save(userProject);
        return userProject;
    }

    public void delete(UserProject userProject) {
        userProjectRepo.delete(userProject);
    }

    public void updateParam(UserProject userProject, Map<String, String> param) {
        if(param.get("is_active") == null){
            throw new NotValidException("Нет поля is_active");
        }
        if(param.get("role") == null){
            throw new NotValidException("Нет поля role");
        }
        userProject.setIsActive(Boolean.valueOf(param.get("is_active")));
        userProject.setTeamRole(TeamRole.valueOf(param.get("role")));
        userProjectRepo.save(userProject);
    }
}
