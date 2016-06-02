package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.UserDTO;
import com.timetracker.Service.Interfaces.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserAPI {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/checkAccess", method = RequestMethod.GET)
    public ResponseEntity checkAccess() {

        return userService.checkAccess(getPrincipalName());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public ResponseEntity getUserList() {

        return userService.getUserList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @RequestMapping(value = "/getParticipantProjectUserList", method = RequestMethod.GET)
    public ResponseEntity getParticipantProjectUserList(@RequestParam(defaultValue = "0") int projectId) {

        return userService.getParticipantProjectUserList(projectId, getPrincipalName());
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @RequestMapping(value = "/getParticipantTaskUserList", method = RequestMethod.GET)
    public ResponseEntity getParticipantTaskUserList(@NotBlank(message = "Необходимо указать ID проекта")
                                                     @RequestParam int taskId) {

        return userService.getParticipantTaskUserList(taskId);
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public ResponseEntity signUp(@RequestBody
                                 @Validated({UserDTO.SignUp.class}) UserDTO signUpUser) {

        return userService.signUp(signUpUser.getUserName(), signUpUser.getEmail(), signUpUser.getPassword());
    }

    public String getPrincipalName() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }
}
