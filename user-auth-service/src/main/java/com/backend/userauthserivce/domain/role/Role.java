package com.backend.userauthserivce.domain.role;


import com.backend.userauthserivce.domain.user.UserModel;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    public Role() {

    }
    public Role(Long id, String name, Set<UserModel> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }
    @Override
    public String getAuthority() {
        return name;
    }

    @ManyToMany(mappedBy = "roles")
    private Set<UserModel> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserModel> getUsers() {
        return users;
    }

    public void setUsers(Set<UserModel> users) {
        this.users = users;
    }
}
