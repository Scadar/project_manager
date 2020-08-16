package ru.scadarnull.project_manager.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "USER_PROJECT")
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User user;

    private Boolean isActive;
    private String teamRole;
}