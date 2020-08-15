package ru.scadarnull.project_manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.scadarnull.project_manager.entity.User;
import ru.scadarnull.project_manager.service.UserService;

import java.math.BigDecimal;

@SpringBootApplication
public class ProjectManagerApplication {


    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerApplication.class, args);
    }


}
