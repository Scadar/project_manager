package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.service.UserService;

import java.math.BigDecimal;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "index";
    }


}
