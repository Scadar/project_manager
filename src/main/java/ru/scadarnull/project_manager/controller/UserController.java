package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.service.AdminService;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public List<User> list(){
        return adminService.findAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getOne(@PathVariable("id") User user){
        if(user == null){
            throw new NotFoundException();
        }
        return user;
    }

    @PostMapping("/user")
    public User create(@RequestBody User user){
        if(!adminService.addUser(user)){
            throw new NotFoundException();
        }
        return user;
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable("id") User userFromDB, @RequestBody User user){
        if(adminService.isExist(user)){
            throw new NotFoundException();
        }
        if(user.getName() != null){
            userFromDB.setName(user.getName());
        }
        if(user.getPassword() != null){
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if(user.getPost() != null){
            userFromDB.setPost(user.getPost());
        }
        if(user.getSalary() != null){
            userFromDB.setSalary(user.getSalary());
        }
        adminService.updateUser(userFromDB);
        return userFromDB;
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") User user){
         adminService.deleteUser(user);
    }
}
