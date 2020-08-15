package ru.scadarnull.project_manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "USER_TASK")
public class UserTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate time;

    @ManyToOne
    private Task task;

    @ManyToOne
    private User user;
}
