package com.timetracker.Entities.DTO;


import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;
import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomStatusValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;

public class TaskDTO {

    @CustomIDValid(groups = {SetName.class, SetDescription.class, SetStatus.class, Default.class})
    private Integer id;

    @CustomIDValid(groups = {Default.class})
    private Integer ancestorProjectId;

    @NotBlank(message = "Необходимо заполнить название проекта",
            groups = {SetName.class, Default.class})
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

    private UserDTO owner;

    private UserDTO executor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAncestorProjectId() {
        return ancestorProjectId;
    }

    public void setAncestorProjectId(Integer ancestorProjectId) {
        this.ancestorProjectId = ancestorProjectId;
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

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public UserDTO getExecutor() {
        return executor;
    }

    public void setExecutor(UserDTO executor) {
        this.executor = executor;
    }

    public interface SetName {
    }

    public interface SetDescription {
    }

    public interface SetStatus {
    }
}
