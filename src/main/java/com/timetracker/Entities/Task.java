package com.timetracker.Entities;


import com.timetracker.Service.AncillaryServices.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
public class Task extends BaseModel {

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "EXECUTOR", referencedColumnName = "ID")
    private User executor;

    @Column(name = "CREATION_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private ZonedDateTime creationDateTime;

    @Column(name = "PLANNED_END_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private ZonedDateTime plannedEndDateTime;

    @Column(name = "ACTUAL_END_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private ZonedDateTime actualEndDateTime;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PATH")
    private String path;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ANCESTOR_TASK_ID",referencedColumnName = "ID", nullable = true)
    Task ancestorTask;

    @OneToMany(mappedBy = "subTasks", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> subTasks;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ANCESTOR_PROJECT_ID", referencedColumnName = "ID")
    Project ancestorProject;

    @OneToMany(mappedBy = "ancestorTask", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reportList;

    @Column(name = "STATUS")
    private String status;

    public Task() {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public ZonedDateTime getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    public void setPlannedEndDateTime(ZonedDateTime plannedEndDateTime) {
        this.plannedEndDateTime = plannedEndDateTime;
    }

    public ZonedDateTime getActualEndDateTime() {
        return actualEndDateTime;
    }

    public void setActualEndDateTime(ZonedDateTime actualEndDateTime) {
        this.actualEndDateTime = actualEndDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Task getAncestorTask() {
        return ancestorTask;
    }

    public void setAncestorTask(Task ancestorTask) {
        this.ancestorTask = ancestorTask;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Task> subTasks) {
        this.subTasks = subTasks;
    }

    public Project getAncestorProject() {
        return ancestorProject;
    }

    public void setAncestorProject(Project ancestorProject) {
        this.ancestorProject = ancestorProject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
