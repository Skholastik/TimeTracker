package com.timetracker.Entities.DTO;


import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;

import javax.validation.groups.Default;

public class UserDTO {

    @CustomIDValid(groups = {AddTaskExecutor.class,Default.class})
    private int id;
    private String name;

    public UserDTO() {
    }

    public UserDTO(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public interface AddTaskExecutor {
    }
}
