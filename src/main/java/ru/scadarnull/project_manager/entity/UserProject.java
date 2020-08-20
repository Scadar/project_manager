package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "USER_PROJECT")
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@userProjectId")
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
    private Project project;

    @ManyToOne
    private User user;
}
