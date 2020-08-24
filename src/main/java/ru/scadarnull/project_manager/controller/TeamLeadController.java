package ru.scadarnull.project_manager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserTask;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.TeamLeadService;
import ru.scadarnull.project_manager.utils.DBUtils;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class TeamLeadController {
    private final TeamLeadService teamLeadService;
    private final DBUtils dbUtils;


    public TeamLeadController(TeamLeadService teamLeadService, DBUtils dbUtils) {
        this.teamLeadService = teamLeadService;
        this.dbUtils = dbUtils;
    }

    @PostMapping("/team_lead")
    public UserTask addRelationship(@AuthenticationPrincipal User user, @RequestBody Map<String, String> param){
        if(!dbUtils.userIsExist(param.get("user"))){
            throw new NotFoundException("Такого юзера нет");
        }
        if(!dbUtils.taskIsExist(param.get("task"))){
            throw new NotFoundException("Такого task`a нет");
        }
        return teamLeadService.addRelationship(user, param);
    }

    @PostMapping("/team_lead/task")
    public Task addTask(@AuthenticationPrincipal User user,
                            @RequestBody @Valid Task task,
                            @RequestParam String project,
                            BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
            throw new NotValidException("Не валидные данные");
        }
        return teamLeadService.addTask(user, task, project);
    }

    @PutMapping("/team_lead/task")
    public Task updateTask(@AuthenticationPrincipal User user,
                           @RequestBody Map<String, String> param){
        if(!dbUtils.taskIsExist(param.get("task"))){
            throw new NotFoundException("Task не найден");
        }
        return teamLeadService.updateTask(user, param);
    }

    @DeleteMapping("/team_lead/task")
    public void deleteTask(@AuthenticationPrincipal User user, @RequestBody Map<String, String> param){
        teamLeadService.deleteTask(user, param);
    }
}
