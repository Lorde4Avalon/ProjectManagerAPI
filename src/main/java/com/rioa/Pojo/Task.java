package com.rioa.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task")
public class Task {
    @Id
    @JsonIgnore
    @Column(name = "taskId")
    @GeneratedValue(generator = "task_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "task_seq", sequenceName = "task_seq", allocationSize = 1)
    private Long id;

    @Column(name = "taskName")
    private String taskName;

    @Column
    @JsonIgnore
    private Integer priority;

    private String afterTask;

    private String continueTime;

    private String startTime;

    private String createPeople;

    private String executePeople;



    @Column(name = "dates")
    @JsonIgnore
    private LocalDateTime dateTime = LocalDateTime.now();

    //user part
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
