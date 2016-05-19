package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.ProjectDTO;
import com.timetracker.Service.Interfaces.ProjectService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping(value = "/api/project", produces = "application/json;charset=UTF-8")
public class ProjectAPI {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/getUserActiveProjectList", method = RequestMethod.GET)
    public ResponseEntity getUserProjectList(@RequestParam
                                             @NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                             String utcOffset) {
        String userName = getPrincipalName();
        return projectService.getUserActiveProjectList(userName, utcOffset);
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    public ResponseEntity createProject(@NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                        @RequestParam String utcOffset,
                                        @RequestBody
                                        @Validated({ProjectDTO.CreateProject.class}) ProjectDTO transferProject) {
        String userName = getPrincipalName();
        return projectService.createProject(transferProject.getName(), utcOffset, userName);
    }

    @RequestMapping(value = "/checkNameInDB", method = RequestMethod.GET)
    public ResponseEntity checkNameInDB(@RequestBody
                                               @Validated({ProjectDTO.CheckNameInDB.class}) ProjectDTO project) {
        String userName = getPrincipalName();
        return projectService.checkNameInDB(project.getName(), userName);
    }

    @RequestMapping(value = "/setName", method = RequestMethod.PUT)
    public ResponseEntity setName(@RequestBody
                                  @Validated({ProjectDTO.SetName.class}) ProjectDTO project) {
        String owner = getPrincipalName();
        return projectService.setName(project.getId(), project.getName(), owner);
    }

    @RequestMapping(value = "/setDescription", method = RequestMethod.PUT)
    public ResponseEntity setDescription(@RequestBody
                                                @Validated({ProjectDTO.SetDescription.class}) ProjectDTO project) {
        String owner = getPrincipalName();
        return projectService.setDescription(project.getId(), project.getDescription(), owner);
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.PUT)
    public ResponseEntity setStatus(@RequestBody
                                           @Validated({ProjectDTO.SetStatus.class}) ProjectDTO project) {
        String owner = getPrincipalName();
        return projectService.setStatus(project.getId(), project.getStatus(), owner);
    }

    @RequestMapping(value = "/checkAccessToChangeProject", method = RequestMethod.GET)
    public ResponseEntity checkAccessToChangeProject(@NotBlank(message = "Необходимо указать ID проекта")
                                                     @RequestParam Integer id) {
        String userName = getPrincipalName();
        return projectService.checkAccessToChangeProject(id, userName);
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
