package com.rioa.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "taskUserRole")
@IdClass(TaskUserRole.PK.class)
public class TaskUserRole {
    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "taskId", nullable = false)
    private Task task;

    private String permission;

    @Data
    public static class PK implements Serializable {
        private long userId;
        private long taskId;
    }
}
