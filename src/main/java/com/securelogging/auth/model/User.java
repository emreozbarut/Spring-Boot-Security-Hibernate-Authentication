package com.securelogging.auth.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String name;

    private String surname;

    private String password;

    private boolean isEnabled;

    @Transient
    private String passwordConfirm;

    @ManyToMany
    private Set<Role> roles;
}
