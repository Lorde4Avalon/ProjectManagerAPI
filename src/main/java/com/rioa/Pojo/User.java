package com.rioa.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "userId")
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username")
    @NotBlank
    private String username;

    @Column(name = "password")
    @NotBlank
    @Pattern(regexp = ".{8,}")
    private String password;

    @Column
    @JsonIgnore
    private LocalDateTime dateTime = LocalDateTime.now();

    //Authentication
    @Column
    private boolean isActive;

    @Column
    @JsonIgnore
    private String roles;
    //task
    @Column(name = "tasks")
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Task> tasks = new ArrayList<>();
}
