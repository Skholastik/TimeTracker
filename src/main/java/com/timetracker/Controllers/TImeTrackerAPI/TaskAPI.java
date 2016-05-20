package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.ProjectDTO;
import com.timetracker.Entities.DTO.TaskDTO;
import com.timetracker.Entities.DTO.UserDTO;
import com.timetracker.Service.Interfaces.TaskService;
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
@RequestMapping(value = "/api/task", produces = "application/json;charset=UTF-8")
public class TaskAPI {


    @Autowired
    TaskService taskService;


    @RequestMapping(value = "/createTask", method = RequestMethod.POST)
    public ResponseEntity createTask(@NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                     @RequestParam String utcOffset,
                                     @RequestBody
                                     @Validated({ProjectDTO.CreateProject.class}) TaskDTO transferTask) {

        String userName = getPrincipalName();
        return taskService.createTask(transferTask.getName(), transferTask.getAncestorProjectId(),
                utcOffset, userName);
    }

    @RequestMapping(value = "/addTaskExecutor", method = RequestMethod.PUT)
    public ResponseEntity addTaskExecutor(@NotBlank(message = "Необходимо указать ID задачи")
                                          @RequestParam int taskId,
                                          @RequestBody
                                          @Validated({UserDTO.AddTaskExecutor.class}) UserDTO transferUser) {

        String owner = getPrincipalName();
        return taskService.addTaskExecutor(taskId, transferUser.getId(), owner);
    }

    @RequestMapping(value = "/getProjectHighTaskList", method = RequestMethod.GET)
    public ResponseEntity getProjectHighTaskList(@NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                                 @RequestParam String utcOffset,
                                                 @NotBlank(message = "Необходимо указать ID родительского проекта")
                                                 @RequestParam int projectId) {

        return taskService.getProjectHighTaskList(projectId, utcOffset);
    }

    @RequestMapping(value = "/getCreatedTaskList", method = RequestMethod.GET)
    public ResponseEntity getCreatedTaskList(@NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                             @RequestParam String utcOffset) {

        String userName = getPrincipalName();
        return taskService.getCreatedTaskList(userName, utcOffset);
    }

    @RequestMapping(value = "/checkNameInDB", method = RequestMethod.GET)
    public ResponseEntity checkNameInDB(@NotBlank(message = "Необходимо указать название задачи")
                                        @RequestParam String taskName,
                                        @NotBlank(message = "Необходимо указать ID родительского проекта")
                                        @RequestParam int ancestorProjectId) {

        return taskService.checkNameInDB(ancestorProjectId, taskName);
    }

    @RequestMapping(value = "/setName", method = RequestMethod.PUT)
    public ResponseEntity setName(@RequestBody
                                  @Validated({TaskDTO.SetName.class}) TaskDTO task) {
        String owner = getPrincipalName();
        return taskService.setName(task.getId(), task.getName(), owner);
    }

    @RequestMapping(value = "/setDescription", method = RequestMethod.PUT)
    public ResponseEntity setDescription(@RequestBody
                                         @Validated({TaskDTO.SetDescription.class}) TaskDTO task) {
        String userName = getPrincipalName();
        return taskService.setDescription(task.getId(), task.getDescription(), userName);
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.PUT)
    public ResponseEntity setStatus(@RequestBody
                                    @Validated({ProjectDTO.SetStatus.class}) TaskDTO task) {
        String userName = getPrincipalName();
        return taskService.setStatus(task.getId(), task.getStatus(), userName);
    }

    @RequestMapping(value = "/checkLowLevelAuthorities", method = RequestMethod.GET)
    public ResponseEntity checkLowLevelAuthorities(@NotBlank(message = "Необходимо указать ID задачи")
                                                   @RequestParam Integer id) {
        String userName = getPrincipalName();
        return taskService.checkLowLevelAuthorities(id, userName);
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
