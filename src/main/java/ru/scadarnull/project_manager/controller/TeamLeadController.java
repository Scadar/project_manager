package ru.scadarnull.project_manager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.entity.UserTask;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.service.TeamLeadService;
import ru.scadarnull.project_manager.utils.DBUtils;

import java.util.Map;

@RestController
public class TeamLeadController {
    private TeamLeadService teamLeadService;
    private DBUtils dbUtils;


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
}
