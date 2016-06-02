package com.timetracker.Entities.DTO;


import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;

public class UserDTO {

    @CustomIDValid(groups = {AddTaskExecutor.class, Default.class})
    private int id;
    @NotBlank(message = "Необходимо указать имя пользователя",
            groups = {SignUp.class, Default.class})
    private String userName;
    @NotBlank(message = "Необходимо указать пароль",
            groups = {SignUp.class, Default.class})
    private String password;
    @NotBlank(message = "Необходимо указать email",
            groups = {SignUp.class, Default.class})
    private String email;

    public UserDTO() {
    }

    public UserDTO(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public interface AddTaskExecutor {
    }

    public interface SignUp {
    }
}
