package com.rioa.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "project")
public class Project {
    @Id
    @Column(name = "projectId")
    @GeneratedValue(generator = "project_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "project_seq", sequenceName = "project_seq", allocationSize = 1)
    private Long projectId;

    @Column(name = "project_name")
    @NotBlank(message = "Project name is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\W].{1,100}$", message = "Project name must be between 1 and 100 characters long and must not contain any special characters")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "project_status")
    private String projectStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id", nullable = false)
    private User projectManager;

    @Column(name = "invite_code")
    @JsonIgnore
    private String inviteCode = UUID.randomUUID().toString().substring(0, 6);



    @Column(name = "project_created_date")
    @JsonIgnore
    private LocalDateTime projectCreatedDate = LocalDateTime.now();

    @Column(name = "project_updated_date")
    //@Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$", message = "Project updated date must be in the format yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectUpdatedDate = LocalDateTime.now();

    @Column(name = "project_end_date")
    //@Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}$", message = "Project end date must be in the format yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectEndDate;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "project_user",
            joinColumns = @JoinColumn(name = "projectId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> users = new HashSet<>();

    @Column(name = "tasks")
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Task> tasks;

    public void copyOf(Project project) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.projectDescription = project.getProjectDescription();
        this.projectStatus = project.getProjectStatus();
        this.projectEndDate = project.getProjectEndDate();
        this.projectManager = project.getProjectManager();
        this.projectCreatedDate = project.getProjectCreatedDate();
        this.projectUpdatedDate = project.getProjectUpdatedDate();
        this.users = project.getUsers();
        this.tasks = project.getTasks();
    }
}
