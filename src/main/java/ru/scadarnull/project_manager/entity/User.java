package ru.scadarnull.project_manager.entity;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@JsonIdentityInfo(generator= ObjectIdGenerators.IntSequenceGenerator.class, property="@userId")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 30)
    private String name;
    @NotNull
    private String password;
    @NotNull
    private BigDecimal salary;
    @NotNull
    private String post;
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<UserTask> userTasks;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserProject> userProjects;

    public User (){}

    public User(String name, String s, BigDecimal bigDecimal, String post, boolean isActive) {
        this.name = name;
        this.password = s;
        this.salary = bigDecimal;
        this.post = post;
        this.isActive = isActive;
    }

    public void addRole(Role role){
        roles.add(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
