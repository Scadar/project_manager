package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.scadarnull.project_manager.entity.Role;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.service.UserService;

import java.math.BigDecimal;

@RestController
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public User addAdmin(){
        User admin = new User("admin", "admin", new BigDecimal(0), "admin");
        admin.addRole(Role.ADMIN);
        userService.addUser(admin);
        return admin;
    }
}
