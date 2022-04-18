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

    @Column
    private String status;

    @Column(name = "afterId")
    private Long afterTask;

    private String continueTime;

    @Pattern(regexp = "[0-9]{4,}-[0-9]{2,}-[0-9]{2,}")
    private String startTime;

    @NotBlank
    private String createPeople;

    private String executePeople;

    @ManyToMany(cascade = CascadeType.DETACH , fetch = FetchType.EAGER)
    @JoinTable(
            name = "taskUser",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> users = new HashSet<>();


    @Column(name = "dates")
    @JsonIgnore
    private LocalDateTime dateTime = LocalDateTime.now();

    //user part
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public void copyOf(Task task) {
        this.taskName = task.getTaskName();
        this.status = task.getStatus();
        this.afterTask = task.getAfterTask();
        this.continueTime = task.getContinueTime();
        this.startTime = task.getStartTime();
        this.createPeople = task.getCreatePeople();
        this.executePeople = task.getExecutePeople();
    }
}
