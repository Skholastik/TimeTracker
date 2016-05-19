package com.timetracker.Entities.DTO;

import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;
import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomStatusValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;

public class ProjectDTO {


    @CustomIDValid(groups = {SetName.class, SetDescription.class,
            SetStatus.class, Default.class})
    private Integer id;

    @NotBlank(message = "Необходимо заполнить название проекта",
            groups = {CreateProject.class, CheckNameInDB.class,
                    SetName.class, Default.class})
    private String name;

    @NotBlank(message = "Необходимо заполнить описание проекта",
            groups = {SetDescription.class, Default.class})
    private String description;

    private String creationDateTime;

    @CustomStatusValid(message = "Такой статус недопустим",
            groups = {SetStatus.class, Default.class})
    private String status;

    private UserDTO owner;

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

    public interface CreateProject {
    }

    public interface SetName {
    }

    public interface SetDescription {
    }

    public interface SetStatus {
    }

    public interface CheckNameInDB {
    }
}

