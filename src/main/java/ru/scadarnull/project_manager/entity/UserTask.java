package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "USER_TASK")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@userTaskId")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Integer time;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User user;
}
