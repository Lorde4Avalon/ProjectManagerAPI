package com.rioa.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "taskId")
    @GeneratedValue(generator = "task_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Long taskId;

    @Column(name = "taskName")
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,100}$", message = "Task name must be alphanumeric, number and less than 100 characters")
    private String taskName;

    @Column(name = "status")
    private String status;

//    @Column(name = "afterId")
//    private Long afterTask;


    //timeline
    @Column(name = "startTime")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$", message = "Start time must be in format yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime = LocalDateTime.now();

    @Column(name = "endTime")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$", message = "End time must be in format yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    //description
    @Column(name = "description")
    private String description;

    @NotBlank
    private String createPeople;

    private String executePeople;

    @ManyToMany(cascade = CascadeType.DETACH , fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> users = new HashSet<>();


    //user part
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private User taskOwner;

    //project part
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;

    public void copyOf(Task task) {
        this.taskName = task.getTaskName();
        this.status = task.getStatus();
        this.createPeople = task.getCreatePeople();
        this.executePeople = task.getExecutePeople();
        this.description = task.getDescription();
        this.startTime = task.getStartTime();
        this.endTime = task.getEndTime();
        this.users = task.getUsers();
        this.taskOwner = task.getTaskOwner();
        this.project = task.getProject();
    }
}
