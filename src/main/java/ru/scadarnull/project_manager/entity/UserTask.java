package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "USER_TASK")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Integer time;

    @ManyToOne
    @JsonIgnoreProperties(value = "userTasks")
    private Task task;

    @ManyToOne
    @JsonIgnoreProperties(value = "userTasks")
    private User user;
}
