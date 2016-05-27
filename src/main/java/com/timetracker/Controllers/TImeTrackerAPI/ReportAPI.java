package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.ReportDTO;
import com.timetracker.Service.Interfaces.ReportService;
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
@RequestMapping(value = "/api/report", produces = "application/json;charset=UTF-8")
public class ReportAPI {

    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/createReport", method = RequestMethod.POST)
    public ResponseEntity createReport(@RequestParam(defaultValue = "0") String utcOffset,
                                       @NotBlank(message = "Необходимо указать ID задания")
                                       @RequestParam int taskId,
                                       @RequestBody
                                       @Validated({ReportDTO.CreateReport.class}) ReportDTO transferReport) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return reportService.createTaskReport(transferReport.getReport(), transferReport.getWorkTime(),
                transferReport.getWorkDate(), taskId, utcOffset, getPrincipalName());
    }

    @RequestMapping(value = "/getTaskAllReportList", method = RequestMethod.GET)
    public ResponseEntity getTaskAllReportList(@RequestParam(defaultValue = "0") String utcOffset,
                                               @NotBlank(message = "Необходимо указать ID задачи")
                                               @RequestParam int taskId) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return reportService.getTaskAllReportList(taskId, utcOffset);
    }

    @RequestMapping(value = "/getProjectReportList", method = RequestMethod.GET)
    public ResponseEntity getProjectReportList(@RequestParam(defaultValue = "0") String projectId,
                                               @RequestParam(defaultValue = "0") String creatorId,
                                               @RequestParam(defaultValue = "0") String startDate,
                                               @RequestParam(defaultValue = "0") String endDate,
                                               @RequestParam(defaultValue = "0") String utcOffset) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return reportService.getProjectReportList(getPrincipalName(), Integer.parseInt(projectId), Integer.parseInt(creatorId),
                startDate, endDate, utcOffset);
    }

    @RequestMapping(value = "/getTaskReportList", method = RequestMethod.GET)
    public ResponseEntity getTaskReportList(@RequestParam(defaultValue = "0") String taskId,
                                            @RequestParam(defaultValue = "0") String creatorId,
                                            @RequestParam(defaultValue = "0") String startDate,
                                            @RequestParam(defaultValue = "0") String endDate,
                                            @RequestParam(defaultValue = "0") String utcOffset) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return reportService.getTaskReportList(getPrincipalName(), Integer.parseInt(taskId), Integer.parseInt(creatorId),
                startDate, endDate, utcOffset);
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
