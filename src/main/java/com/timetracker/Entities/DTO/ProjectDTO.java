package com.timetracker.Entities.DTO;

import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;
import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomStatusValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;

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

    private UserDTO creator;

    Set<TaskDTO> taskList = new HashSet<>();

    public void addTask(TaskDTO task) {
        taskList.add(task);
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

    public Set<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<TaskDTO> taskList) {
        this.taskList = taskList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ProjectDTO)) return false;

        ProjectDTO that = (ProjectDTO) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
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

