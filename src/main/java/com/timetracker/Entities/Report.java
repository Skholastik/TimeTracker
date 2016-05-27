package com.timetracker.Entities;


import com.timetracker.Service.AncillaryServices.LocalDateAttributeConverter;
import com.timetracker.Service.AncillaryServices.LocalDateTimeAttributeConverter;
import com.timetracker.Service.AncillaryServices.LocalTimeAttributeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "reports")

public class Report extends BaseModel {

    @Column(name = "REPORT")
    String report;

    @Column(name = "WORK_TIME")
    @Convert(converter = LocalTimeAttributeConverter.class)
    LocalTime workTime;

    @Column(name = "WORK_DATE")
    @Convert(converter = LocalDateAttributeConverter.class)
    LocalDate workDate;

    @Column(name = "CREATION_DATE")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    ZonedDateTime creationDateTime;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "REPORTER", referencedColumnName = "ID")
    User reporter;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ANCESTOR_TASK_ID", referencedColumnName = "ID", nullable = true)
    Task ancestorTask;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ANCESTOR_PROJECT_ID", referencedColumnName = "ID")
    Project ancestorProject;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public LocalTime getWorkTime() {
        return workTime;
    }

    public void setWorkTime(LocalTime workTime) {
        this.workTime = workTime;
    }

    public LocalDate getWorkDate() {
        return workDate;
    }

    public void setWorkDate(LocalDate workDate) {
        this.workDate = workDate;
    }

    public ZonedDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(ZonedDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public Task getAncestorTask() {
        return ancestorTask;
    }

    public void setAncestorTask(Task ancestorTask) {
        this.ancestorTask = ancestorTask;
    }

    public Project getAncestorProject() {
        return ancestorProject;
    }

    public void setAncestorProject(Project ancestorProject) {
        this.ancestorProject = ancestorProject;
    }
}
