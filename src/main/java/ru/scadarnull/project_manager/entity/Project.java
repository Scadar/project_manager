package ru.scadarnull.project_manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private LocalDate startTime;

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjects;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

}
