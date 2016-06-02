package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.ProjectDTO;
import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomStatusValid;
import com.timetracker.Service.Interfaces.ProjectService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;


@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping(value = "/api/project", produces = "application/json;charset=UTF-8")
public class ProjectAPI {

    @Autowired
    ProjectService projectService;

    @RequestMapping(value = "/getUserProjectListByStatus", method = RequestMethod.GET)
    public ResponseEntity getUserProjectListByStatus(@RequestParam @CustomStatusValid String status,
                                                     @RequestParam(defaultValue = "0") String utcOffset) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return projectService.getUserProjectListByStatus(getPrincipalName(), status, utcOffset);
    }

    @RequestMapping(value = "/getCreatedProjectList", method = RequestMethod.GET)
    public ResponseEntity getCreatedProjectList(@RequestParam(defaultValue = "0") String utcOffset) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return projectService.getCreatedProjectList(getPrincipalName(), utcOffset);
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    public ResponseEntity createProject(@RequestParam(defaultValue = "0") String utcOffset,
                                        @RequestBody
                                        @Validated({ProjectDTO.CreateProject.class}) ProjectDTO transferProject) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return projectService.createProject(transferProject.getName(), utcOffset, getPrincipalName());
    }

    @RequestMapping(value = "/checkNameInDB", method = RequestMethod.GET)
    public ResponseEntity checkNameInDB(@RequestBody
                                        @Validated({ProjectDTO.CheckNameInDB.class}) ProjectDTO project) {

        return projectService.checkNameInDB(project.getName(), getPrincipalName());
    }

    @RequestMapping(value = "/setName", method = RequestMethod.PUT)
    public ResponseEntity setName(@RequestBody
                                  @Validated({ProjectDTO.SetName.class}) ProjectDTO project) {

        return projectService.setName(project.getId(), project.getName(), getPrincipalName());
    }

    @RequestMapping(value = "/setDescription", method = RequestMethod.PUT)
    public ResponseEntity setDescription(@RequestBody
                                         @Validated({ProjectDTO.SetDescription.class}) ProjectDTO project) {

        return projectService.setDescription(project.getId(), project.getDescription(), getPrincipalName());
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.PUT)
    public ResponseEntity setStatus(@RequestBody
                                    @Validated({ProjectDTO.SetStatus.class}) ProjectDTO project) {

        return projectService.setStatus(project.getId(), project.getStatus(), getPrincipalName());
    }

    @RequestMapping(value = "/checkAccessToChangeProject", method = RequestMethod.GET)
    public ResponseEntity checkAccessToChangeProject(@NotBlank(message = "Необходимо указать ID проекта")
                                                     @RequestParam Integer id) {

        return projectService.checkAccessToChangeProject(id, getPrincipalName());
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

    public String getCurrentOffset() {
        return ZonedDateTime.now().getOffset().toString();
    }

}
