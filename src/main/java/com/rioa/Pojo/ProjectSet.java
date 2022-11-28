package com.rioa.Pojo;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class ProjectSet {
    private List<Long> projectIdList;

    public ProjectSet() {
    }

    public ProjectSet(List<Long> projectIdList) {
        this.projectIdList = projectIdList;
    }

    public List<Long> getProjectIdList() {
        return projectIdList;
    }

    public void setProjectIdList(List<Long> projectIdList) {
        this.projectIdList = projectIdList;
    }
}
