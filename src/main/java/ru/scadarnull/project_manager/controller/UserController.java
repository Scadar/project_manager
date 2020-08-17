package ru.scadarnull.project_manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.exceptions.IsExistException;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.AdminService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user")
    public List<User> list(){
        return adminService.findAllUsers();
    }

    @GetMapping("/user/{id}")
    public User getOne(@PathVariable("id") User user){
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    @PostMapping("/user")
    public User create(@RequestBody @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new NotValidException("Невалидные данные");
        }
        if(!adminService.addUser(user)){
            throw new IsExistException("Такой пользователь уже есть");
        }
        return user;
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable("id") User userFromDB, @RequestBody User user){

        if(adminService.userIsExist(user) && !userFromDB.getName().equals(user.getName())){
            throw new IsExistException("Такой пользователь уже есть");
        }
        if(user.getName() != null){
            if(user.getName().length() < 3 || user.getName().length() > 30){
                throw new NotValidException("Невалидные данные");
            }
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
