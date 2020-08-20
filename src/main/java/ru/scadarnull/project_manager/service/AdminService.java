package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Role;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.repo.AdminRepo;

import java.util.Collections;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean addUser(User user){
        if(userIsExist(user)){
            return false;
        }
        user.addRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        adminRepo.save(user);
        return true;
    }

    public List<User> findAllUsers(){
        return adminRepo.findAll();
    }

    public void deleteUser(User user) {
        adminRepo.delete(user);
    }

    public void updateUser(User user) {
        adminRepo.save(user);
    }

    public boolean userIsExist(User user){
        User userFromDb = adminRepo.findByName(user.getUsername());
        return userFromDb != null;
    }
}
