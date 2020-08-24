package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.repo.UserProjectRepo;

import java.util.List;
import java.util.Map;

@Service
public class UserProjectService {

    private UserProjectRepo userProjectRepo;
    private UserService userService;
    private ProjectService projectService;

    @Autowired
    public UserProjectService(UserProjectRepo userProjectRepo, UserService userService, ProjectService projectService) {
        this.userProjectRepo = userProjectRepo;
        this.userService = userService;
        this.projectService = projectService;
    }

    public List<UserProject> findAll() {
        return userProjectRepo.findAll();
    }

    public UserProject add(Map<String, String> param) {
        User user = userService.findByName(param.get("user"));
        Project project = projectService.findByName(param.get("project"));
        Boolean is_active = Boolean.valueOf(param.get("is_active"));
        TeamRole teamRole = TeamRole.valueOf(param.get("role"));
        UserProject userProject = new UserProject();
        userProject.setUser(user);
        userProject.setProject(project);
        userProject.setIsActive(is_active);
        userProject.setTeamRole(teamRole);
        userProjectRepo.save(userProject);
        return userProject;
    }

    public void delete(UserProject userProject) {
        userProjectRepo.delete(userProject);
    }

    public void updateParam(UserProject userProject, Map<String, String> param) {
        Boolean is_active = Boolean.valueOf(param.get("is_active"));
        TeamRole teamRole = TeamRole.valueOf(param.get("role"));
        if(is_active != null){
            userProject.setIsActive(is_active);
        }
        if(teamRole != null){
            userProject.setTeamRole(teamRole);
        }
        userProjectRepo.save(userProject);
    }
}
