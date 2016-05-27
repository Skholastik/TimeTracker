package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Service.Interfaces.UserService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping(value = "/api/user", produces = "application/json;charset=UTF-8")
public class UserAPI {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public ResponseEntity getUserList() {

        return userService.getUserList();
    }

    @RequestMapping(value = "/getParticipantProjectUserList", method = RequestMethod.GET)
    public ResponseEntity getParticipantProjectUserList(@RequestParam(defaultValue = "0") int projectId) {

        return userService.getParticipantProjectUserList(projectId, getPrincipalName());
    }

    @RequestMapping(value = "/getParticipantTaskUserList", method = RequestMethod.GET)
    public ResponseEntity getParticipantTaskUserList(@NotBlank(message = "Необходимо указать ID проекта")
                                                     @RequestParam int taskId) {

        return userService.getParticipantTaskUserList(taskId);
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
