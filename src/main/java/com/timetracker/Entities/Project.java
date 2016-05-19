package com.timetracker.Entities;


import com.timetracker.Service.AncillaryServices.LocalDateTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project extends BaseModel {

    @Column(name = "NAME")
    String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID")
    User owner;

    @Column(name = "CREATION_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    ZonedDateTime creationDateTime;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "STATUS")
    private String status;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProjectParticipants> participantList;

    @OneToMany(mappedBy = "ancestorProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> taskList;

    @OneToMany(mappedBy = "ancestorProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Report> reportList;

    public void addTask(Task newTask){
        newTask.setAncestorProject(this);
        taskList.add(newTask);
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

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProjectParticipants> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<ProjectParticipants> participantList) {
        this.participantList = participantList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}

