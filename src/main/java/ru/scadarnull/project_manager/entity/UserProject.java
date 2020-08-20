package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "USER_PROJECT")
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TeamRole teamRole;

    @ManyToOne
    @JsonIgnoreProperties(value = "userProjects")
    private Project project;

    @ManyToOne
    @JsonIgnoreProperties(value = "userProjects")
    private User user;
}
