package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.UserProject;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.service.UserProjectService;
import ru.scadarnull.project_manager.utils.DBUtils;

import java.util.List;
import java.util.Map;

@RestController
public class UserProjectController {

    private UserProjectService userProjectService;
    private DBUtils dbUtils;

    @Autowired
    public UserProjectController(UserProjectService userProjectService, DBUtils dbUtils) {
        this.userProjectService = userProjectService;
        this.dbUtils = dbUtils;
    }

    @GetMapping("/user_project")
    public List<UserProject> showAll(){
        return userProjectService.findAll();
    }

    @GetMapping("/user_project/{id}")
    public UserProject getOne(@PathVariable("id") UserProject userProject){
        if(userProject == null){
            throw new NotFoundException("UserProject не найден");
        }
        return userProject;
    }

    @PostMapping("/user_project")
    public UserProject create(@RequestBody Map<String, String> param){
        if(!dbUtils.userIsExist(param.get("user"))){
            throw new NotFoundException("Пользователь не найден");
        }
        if(!dbUtils.projectIsExist(param.get("project"))){
            throw new NotFoundException("Project не найден");
        }
        UserProject userProject = userProjectService.add(param);
        return userProject;
    }

    @PutMapping("/user_project/{id}")
    public UserProject update(@PathVariable("id") UserProject userProject,
                           @RequestBody Map<String, String> param) {
        userProjectService.updateParam(userProject, param);
        return userProject;
    }

    @DeleteMapping("/user_project/{id}")
    public void delete(@PathVariable("id") UserProject userProject){
        userProjectService.delete(userProject);
    }
}
