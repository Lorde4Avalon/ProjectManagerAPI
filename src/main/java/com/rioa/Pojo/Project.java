package com.rioa.Pojo;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project")
public class Project {
    @Id
    @Column(name = "projectId")
    @GeneratedValue(generator = "project_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "project_id_seq", sequenceName = "project_id_seq", allocationSize = 1)
    private Long projectId;

    @Column(name = "project_name")
    @NotBlank(message = "Project name is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,100}$", message = "Project name must be between 1 and 100 characters long and must not contain any special characters")
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "project_status")
    private String projectStatus;

    @Column(name = "project_start_date")
    private String projectStartDate;

    @Column(name = "project_end_date")
    private String projectEndDate;

    @Column(name = "project_manager")
    private String projectManager;

    @Column(name = "project_created_date")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$", message = "Project created date must be in the format yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectCreatedDate;

    @Column(name = "project_updated_date")
    @Pattern(regexp = "^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}$", message = "Project updated date must be in the format yyyy-MM-dd HH:mm:ss")
    private LocalDateTime projectUpdatedDate;

    public void copyOf(Project project) {
        this.projectId = project.getProjectId();
        this.projectName = project.getProjectName();
        this.projectDescription = project.getProjectDescription();
        this.projectStatus = project.getProjectStatus();
        this.projectStartDate = project.getProjectStartDate();
        this.projectEndDate = project.getProjectEndDate();
        this.projectManager = project.getProjectManager();
        this.projectCreatedDate = project.getProjectCreatedDate();
        this.projectUpdatedDate = project.getProjectUpdatedDate();
    }
}
