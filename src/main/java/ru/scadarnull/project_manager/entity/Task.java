package ru.scadarnull.project_manager.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
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

    @Max(value = 200)
    private String description;

    private LocalDate actualEndTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private State state;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "task")
    private List<UserTask> userTasks;
}
