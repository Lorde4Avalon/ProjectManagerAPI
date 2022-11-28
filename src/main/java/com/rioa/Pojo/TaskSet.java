package com.rioa.Pojo;

import java.util.List;

public class TaskSet {
    private List<Long> taskIdList;

    public TaskSet(List<Long> taskIdList) {
        this.taskIdList = taskIdList;
    }

    public List<Long> getTaskIdList() {
        return taskIdList;
    }

    public void setTaskIdList(List<Long> taskIdList) {
        this.taskIdList = taskIdList;
    }
}
