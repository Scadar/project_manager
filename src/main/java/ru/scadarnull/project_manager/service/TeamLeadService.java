package ru.scadarnull.project_manager.service;

import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.*;
import ru.scadarnull.project_manager.exceptions.ForbiddenException;
import ru.scadarnull.project_manager.repo.*;

import java.util.List;
import java.util.Map;

@Service
public class TeamLeadService {

    private AdminRepo adminRepo;
    private TaskRepo taskRepo;
    private ProjectRepo projectRepo;
    private UserProjectRepo userProjectRepo;
    private UserTaskRepo userTaskRepo;

    public TeamLeadService(AdminRepo adminRepo, TaskRepo taskRepo, ProjectRepo projectRepo, UserProjectRepo userProjectRepo, UserTaskRepo userTaskRepo) {
        this.adminRepo = adminRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
        this.userProjectRepo = userProjectRepo;
        this.userTaskRepo = userTaskRepo;
    }

    public UserTask addRelationship(User teamLead, Map<String, String> param) {
        User user = adminRepo.findByName(param.get("user"));
        Task task = taskRepo.findByName(param.get("task"));
        Project project = task.getProject();
        List<Project> teamLeadProjects = projectRepo.findProjectByUser(teamLead);
        List<Project> userProjects = projectRepo.findProjectByUser(user);
        if(teamLeadProjects.contains(project) && userProjects.contains(project)){
            for(UserProject up : userProjectRepo.findProjectByUser(teamLead)){
                if(up.getProject().equals(project)){
                    if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                        UserTask userTask = new UserTask();
                        userTask.setTime(0);
                        userTask.setTask(task);
                        userTask.setUser(user);
                        userTaskRepo.save(userTask);
                        return userTask;
                    }else{
                        throw new ForbiddenException("Вы не тимлид");
                    }
                }
            }
            throw new ForbiddenException("Вас нет в данном проекте");
        }
        throw new ForbiddenException("Либо тимлида нет в проекте, либо юзера, а мб и обоих");
    }

    public Task addTask(User user, Task task, String projectName) {
        Project project = projectRepo.findByName(projectName);
        List<Project> userProjects = projectRepo.findProjectByUser(user);
        if(userProjects.contains(project)){
            for(UserProject up : userProjectRepo.findProjectByUser(user)){
                if(up.getProject().equals(project)){
                    if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                        task.setProject(project);
                        taskRepo.save(task);
                        return task;
                    }else{
                        throw new ForbiddenException("Вы не тимлид");
                    }
                }
            }
        }
        throw new ForbiddenException("Не ваш проект");
    }

    public Task updateTask(User user, Map<String, String> param) {
        Task task = taskRepo.findByName(param.get("task"));
        Project project = task.getProject();
        List<Project> userProjects = projectRepo.findProjectByUser(user);
        if(userProjects.contains(project)){
            for(UserProject up : userProjectRepo.findProjectByUser(user)){
                if(up.getProject().equals(project)){
                    if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                        if(param.get("state") != null){
                            task.setState(State.valueOf(param.get("state")));
                        }
                        if(param.get("name") != null){
                            task.setName(param.get("name"));
                        }
                        if(param.get("description") != null){
                            task.setDescription(param.get("description"));
                        }
                        taskRepo.save(task);
                        return task;
                    }else{
                        throw new ForbiddenException("Вы не тимлид");
                    }
                }
            }
        }
        throw new ForbiddenException("Это не ваш проект");
    }

    public void deleteTask(User user, Map<String, String> param) {
        Task task = taskRepo.findByName(param.get("task"));
        Project project = task.getProject();
        List<Project> userProjects = projectRepo.findProjectByUser(user);
        if(userProjects.contains(project)){
            for(UserProject up : userProjectRepo.findProjectByUser(user)){
                if(up.getProject().equals(project)){
                    if(up.getTeamRole().equals(TeamRole.TEAM_LEAD)){
                        taskRepo.delete(task);
                    }else{
                        throw new ForbiddenException("Вы не тимлид");
                    }
                }
            }
        }
        throw new ForbiddenException("Это не ваш проект");
    }
}
