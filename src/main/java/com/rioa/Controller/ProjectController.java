package com.rioa.Controller;

import com.rioa.Pojo.Project;
import com.rioa.dao.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping("/create")
    public Map<String, Long> createProject(@Valid @RequestBody Project project){
        projectRepository.save(project);
        return Collections.singletonMap("id", project.getProjectId());
    }

    @PutMapping("/update/{id}")
    public void updateProject(@Valid @RequestBody Project project, @PathVariable Long id){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        Project oldProject = projectRepository.findByProjectId(id).get();
        oldProject.copyOf(project);
        oldProject.setProjectUpdatedDate(LocalDateTime.now());
        projectRepository.save(oldProject);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable Long id){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        projectRepository.deleteById(id);
    }

    @GetMapping("/get/{id}")
    public Project getProject(@PathVariable Long id){
        if (projectRepository.findByProjectId(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project not found");
        }

        return projectRepository.findByProjectId(id).get();
    }
}
