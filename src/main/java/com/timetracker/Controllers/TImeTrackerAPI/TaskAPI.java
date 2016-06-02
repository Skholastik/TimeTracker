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

import java.time.ZonedDateTime;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping(value = "/api/task", produces = "application/json;charset=UTF-8")
public class TaskAPI {

    @Autowired
    TaskService taskService;

    @RequestMapping(value = "/createTask", method = RequestMethod.POST)
    public ResponseEntity createTask(@RequestParam(defaultValue = "0") String utcOffset,
                                     @NotBlank(message = "Необходимо указать ID родительской задачи")
                                     @RequestParam int ancestorProjectId,
                                     @RequestBody
                                     @Validated({TaskDTO.CreateTask.class}) TaskDTO transferTask) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return taskService.createTask(transferTask.getName(), ancestorProjectId,
                utcOffset, getPrincipalName());
    }

    @RequestMapping(value = "/addTaskExecutor", method = RequestMethod.PUT)
    public ResponseEntity addTaskExecutor(@NotBlank(message = "Необходимо указать ID задачи")
                                          @RequestParam int taskId,
                                          @RequestBody
                                          @Validated({UserDTO.AddTaskExecutor.class}) UserDTO transferUser) {

        return taskService.addTaskExecutor(taskId, transferUser.getId(), getPrincipalName());

    }

    @RequestMapping(value = "/getProjectHighTaskList", method = RequestMethod.GET)
    public ResponseEntity getProjectHighTaskList(@RequestParam(defaultValue = "0") String utcOffset,
                                                 @NotBlank(message = "Необходимо указать ID родительского проекта")
                                                 @RequestParam int projectId) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return taskService.getProjectHighTaskList(projectId, utcOffset);
    }

    @RequestMapping(value = "/getCreatedTaskList", method = RequestMethod.GET)
    public ResponseEntity getCreatedTaskList(@RequestParam(defaultValue = "0") String utcOffset) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return taskService.getCreatedTaskList(getPrincipalName(), utcOffset);
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

        return taskService.setName(task.getId(), task.getName(), getPrincipalName());
    }

    @RequestMapping(value = "/setDescription", method = RequestMethod.PUT)
    public ResponseEntity setDescription(@RequestBody
                                         @Validated({TaskDTO.SetDescription.class}) TaskDTO task) {

        return taskService.setDescription(task.getId(), task.getDescription(), getPrincipalName());
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.PUT)
    public ResponseEntity setStatus(@RequestBody
                                    @Validated({ProjectDTO.SetStatus.class}) TaskDTO task) {

        return taskService.setStatus(task.getId(), task.getStatus(), getPrincipalName());
    }

    @RequestMapping(value = "/checkLowLevelAuthorities", method = RequestMethod.GET)
    public ResponseEntity checkLowLevelAuthorities(@NotBlank(message = "Необходимо указать ID задачи")
                                                   @RequestParam Integer id) {

        return taskService.checkLowLevelAuthorities(id, getPrincipalName());
    }

    @RequestMapping(value = "/checkHighLevelAuthorities", method = RequestMethod.GET)
    public ResponseEntity checkHighLevelAuthorities(@NotBlank(message = "Необходимо указать ID задачи")
                                                   @RequestParam Integer id) {

        return taskService.checkHighLevelAuthorities(id, getPrincipalName());
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
