package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.scadarnull.project_manager.entity.Role;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.service.AdminService;

import java.math.BigDecimal;

@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/test")
    public User addAdmin(){
        User admin = new User("admin", "admin", new BigDecimal(0), "admin");
        admin.addRole(Role.ADMIN);
        adminService.addUser(admin);
        return admin;
    }
}
