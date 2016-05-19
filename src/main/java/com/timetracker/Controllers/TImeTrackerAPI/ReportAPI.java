package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.ReportDTO;
import com.timetracker.Service.Interfaces.ReportService;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','USER')")
@RequestMapping(value = "/api/report", produces = "application/json;charset=UTF-8")
public class ReportAPI {

    @Autowired
    ReportService reportService;

    @RequestMapping(value = "/createReport", method = RequestMethod.POST)
    public ResponseEntity createReport(@NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                       @RequestParam String utcOffset,
                                       @NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                       @RequestParam int taskId,
                                       @RequestBody
                                       @Validated({ReportDTO.CreateReport.class}) ReportDTO transferReport) {

        String userName = getPrincipalName();
        return reportService.createTaskReport(transferReport.getReport(), transferReport.getWorkTime(),
                transferReport.getWorkDate(), taskId, utcOffset, userName);
    }

    @RequestMapping(value = "/getTaskAllReportList", method = RequestMethod.GET)
    public ResponseEntity getTaskAllReportList(@NotBlank(message = "Необходимо указать вашу временную зону (UTC)")
                                                       @RequestParam String utcOffset,
                                                       @NotBlank(message = "Необходимо указать ID задачи")
                                                       @RequestParam int taskId) {

        return reportService.getTaskAllReportList(taskId, utcOffset);
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
