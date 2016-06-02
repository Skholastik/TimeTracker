package com.timetracker.Entities.DTO;


import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;
import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomStatusValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;

public class TaskDTO {

    @CustomIDValid(groups = {SetName.class, SetDescription.class, SetStatus.class, Default.class})
    private Integer id;

    @NotBlank(message = "Необходимо заполнить название проекта",
            groups = {CreateTask.class, SetName.class, Default.class})
    private String name;

    @NotBlank(message = "Необходимо заполнить описание проекта",
            groups = {SetDescription.class, Default.class})
    private String description;

    private String creationDateTime;

    private String plannedEndDateTime;

    private String actualEndDateTime;

    @CustomStatusValid(message = "Такой статус недопустим",
            groups = {SetStatus.class, Default.class})
    private String status;

    private UserDTO creator;

    private UserDTO executor;

    private Set<ReporterDTO> reporterList = new HashSet<>();

    public void addReporter(ReporterDTO reporter) {
        reporterList.add(reporter);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    public void setPlannedEndDateTime(String plannedEndDateTime) {
        this.plannedEndDateTime = plannedEndDateTime;
    }

    public String getActualEndDateTime() {
        return actualEndDateTime;
    }

    public void setActualEndDateTime(String actualEndDateTime) {
        this.actualEndDateTime = actualEndDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public UserDTO getExecutor() {
        return executor;
    }

    public void setExecutor(UserDTO executor) {
        this.executor = executor;
    }

    public Set<ReporterDTO> getReporterList() {
        return reporterList;
    }

    public void setReporterList(Set<ReporterDTO> reporterList) {
        this.reporterList = reporterList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskDTO)) return false;

        TaskDTO taskDTO = (TaskDTO) o;

        return getId().equals(taskDTO.getId());

    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();

    }

    public interface CreateTask {

    }

    public interface SetName {
    }

    public interface SetDescription {
    }

    public interface SetStatus {
    }

}
