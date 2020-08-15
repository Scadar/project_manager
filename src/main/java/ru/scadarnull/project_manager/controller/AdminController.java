package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.service.AdminService;

import java.util.Objects;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin")
    public String forAdmin(){
        return "admin";
    }

    @GetMapping("/admin/add_user")
    public String showAddUser(Model model, @RequestParam(required = false) User user){
        model.addAttribute("user", Objects.requireNonNullElseGet(user, User::new));
        return "add_user";
    }

    @PostMapping("/admin/add_user")
    public String addUser(@ModelAttribute User user, Model model){
        if(!adminService.addUser(user)){
            model.addAttribute("user", user);
            model.addAttribute("userExists", "Такой пользователь уже существует");
            return "add_user";
        }
        model.addAttribute("refInfo", "Пользователь добавлен");
        return "show_users";
    }

    @GetMapping("/admin/show_users")
    public String showUsers(Model model){
        model.addAttribute("users", adminService.findAllUsers());
        return "show_users";
    }

    @GetMapping("/admin/user/delete/{user}")
    public String deleteUser(@PathVariable User user){
        adminService.deleteUser(user);
        return "redirect:/admin/show_users";
    }

    @GetMapping("/admin/user/update/{user}")
    public String showUpdateUser(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        return "update_user";
    }

    @PostMapping("/admin/user/update")
    public String updateUser(@ModelAttribute User user, Model model){
        adminService.updateUser(user);
        return "show_users";
    }
}
