package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private LocalDate startTime;

    @OneToMany(mappedBy = "project")
    @JsonIgnoreProperties(value = "project")
    private List<UserProject> userProjects;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

}
