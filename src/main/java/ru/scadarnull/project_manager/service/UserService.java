package ru.scadarnull.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.repo.UserRepo;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepo.findByName(name);
        if(user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return user;
    }
}
