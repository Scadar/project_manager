package ru.scadarnull.project_manager.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.exceptions.IsExistException;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.exceptions.NotValidException;
import ru.scadarnull.project_manager.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user")
    public List<User> list(){
        return userService.findAllUsers();
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
        if(!userService.addUser(user)){
            throw new IsExistException("Такой пользователь уже есть");
        }
        return user;
    }

    @PutMapping("/user/{id}")
    public User update(@PathVariable("id") User userFromDB, @RequestBody User user){

        if(userService.userIsExist(user) && !userFromDB.getName().equals(user.getName())){
            throw new IsExistException("Такой пользователь уже есть");
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
        userService.updateUser(userFromDB);
        return userFromDB;
    }

    @GetMapping("/user/{userId}/tasks")
    public List<Task> getTasksByUser(@PathVariable("userId") User user){
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        return userService.getTasksByUser(user);
    }

    @GetMapping("/user/{userId}/projects")
    public List<Project> getProjectsByUser(@PathVariable("userId") User user){
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        return userService.getProjectsByUser(user);
    }

    @GetMapping("/user/{userId}/projects/{projectId}/tasks")
    public List<Task> getTaskByUserAndProject(@PathVariable("userId") User user,
                                              @PathVariable("projectId") Project project){
        if(user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        if(project == null){
            throw new NotFoundException("Проект не найден");
        }
        return userService.getTaskByUserAndProject(user, project);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable("id") User user){
        userService.deleteUser(user);
    }
}
