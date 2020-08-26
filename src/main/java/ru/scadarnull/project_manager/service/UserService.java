package ru.scadarnull.project_manager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Project;
import ru.scadarnull.project_manager.entity.Role;
import ru.scadarnull.project_manager.entity.Task;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.exceptions.NotFoundException;
import ru.scadarnull.project_manager.repo.ProjectRepo;
import ru.scadarnull.project_manager.repo.TaskRepo;
import ru.scadarnull.project_manager.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, TaskRepo taskRepo, ProjectRepo projectRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepo.findByName(name);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }

    public boolean addUser(User user){
        if(userIsExist(user)){
            return false;
        }
        user.addRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return true;
    }

    public List<User> findAllUsers(){
        return userRepo.findAll();
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public void updateUser(User user) {
        userRepo.save(user);
    }

    public boolean userIsExist(User user){
        return userIsExist(user.getName());
    }

    public boolean userIsExist(String user){
        User userFromDb = findByName(user);
        return userFromDb != null;
    }

    public User findByName(String name){
        return userRepo.findByName(name);
    }

    public List<Task> getTasksByUser(User user) {
        return taskRepo.findTaskByUser(user);
    }

    public List<Project> getProjectsByUser(User user) {
        return projectRepo.findProjectsByUser(user);
    }

    public List<Task> getTaskByUserAndProject(User user, Project project) {
        List<Task> result = new ArrayList<>();
        List<Project> projects = getProjectsByUser(user);
        List<Task> userTasks = getTasksByUser(user);
        if(projects.contains(project)){
            for(Task task : project.getTasks()){
                if(userTasks.contains(task)){
                    result.add(task);
                }
            }
            return result;
        }
        throw new NotFoundException("У такого юзера нет этого проекта");
    }
}
