package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 30)
    private String name;

    private String description;

    private LocalDate actualEndTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private State state;

    @ManyToOne
    @JsonIgnoreProperties("tasks")
    @JoinColumn(nullable = false)
    private Project project;

    @OneToMany(mappedBy = "task")
    @JsonIgnoreProperties(value = "task")
    private List<UserTask> userTasks;
}
