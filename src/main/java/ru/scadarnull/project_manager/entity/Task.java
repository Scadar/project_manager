package ru.scadarnull.project_manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private BigDecimal estimatedCosts;
    private LocalDate actualEndTime;


    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "task")
    private List<UserTask> userTasks;
}
