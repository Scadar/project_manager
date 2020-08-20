package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@projectId")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private LocalDate startTime;

    @OneToMany(mappedBy = "project")
    private List<UserProject> userProjects;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

}
