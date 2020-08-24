package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.Role;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.repo.UserRepo;

import java.util.List;


@Service
public class UserService implements UserDetailsService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
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
        User userFromDb = userRepo.findByName(user.getUsername());
        return userFromDb != null;
    }

    public boolean userIsExist(String user){
        User userFromDb = userRepo.findByName(user);
        return userFromDb != null;
    }

    public User findByName(String name){
        return userRepo.findByName(name);
    }
}
