package ru.scadarnull.project_manager.service;

import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.UserProjectRepo;
import ru.scadarnull.project_manager.repo.UserRepo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final UserProjectRepo userProjectRepo;

    public ProjectService(ProjectRepo projectRepo, UserRepo userRepo, UserProjectRepo userProjectRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.userProjectRepo = userProjectRepo;
    }

    public List<Project> getAll() {
        return projectRepo.findAll();
    }

    public List<Project> getAll(User user) {
        return projectRepo.findProjectsByUser(user);
    }

    public boolean addProject(Project project) {
        if(projectIsExist(project)){
            return false;
        }
        projectRepo.save(project);
        return true;
    }

    public boolean projectIsExist(Project project){
        return projectIsExist(project.getName());
    }

    public boolean projectIsExist(String project){
        Project projectFromDb = findByName(project);
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

    public List<User> getUsersByProject(Project project) {
        return userRepo.getUsersByProject(project);
    }

    public UserProject addUserInProject(User user, Project project, Map<String, String> params) {
        UserProject userProject = new UserProject();
        userProject.setProject(project);
        userProject.setUser(user);
        userProject.setTeamRole(TeamRole.valueOf(params.get("teamRole")));
        userProject.setIsActive(Boolean.valueOf(params.get("isActive")));
        return userProjectRepo.save(userProject);
    }

    public UserProject updateUserInProject(User user, Project project, Map<String, String> params) {
        UserProject userProject = userProjectRepo.findByUserAndProject(user, project);
        if(params.get("teamRole") != null){
            userProject.setTeamRole(TeamRole.valueOf(params.get("teamRole")));
        }
        if(params.get("isActive") != null){
            userProject.setIsActive(Boolean.valueOf(params.get("isActive")));
        }
        return userProjectRepo.save(userProject);
    }

    public Map<State, Integer> statistics(Project project) {
        Map<State, Integer> result = new LinkedHashMap<>();

        for(Task task : project.getTasks()){
            switch (task.getState()){
                case UNDER_CONSIDERATION: result.merge(State.UNDER_CONSIDERATION, 1, Integer::sum); break;
                case COMPLETED: result.merge(State.COMPLETED, 1, Integer::sum); break;
                case IN_WORK: result.merge(State.IN_WORK, 1, Integer::sum); break;
                case CONFIRMED: result.merge(State.CONFIRMED, 1, Integer::sum); break;
            }
        }

        return result;
    }
}
