package com.timetracker.Controllers.TImeTrackerAPI;

import com.timetracker.Entities.DTO.ReportDTO;
import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomReportTypeValid;
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

    @RequestMapping(value = "/getTaskReporterList", method = RequestMethod.GET)
    public ResponseEntity getTaskReporterList(@RequestParam(defaultValue = "0") String utcOffset,
                                              @NotBlank(message = "Необходимо указать ID задачи")
                                              @RequestParam int taskId) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return reportService.getTaskReporterList(taskId, utcOffset);
    }

    @RequestMapping(value = "/getReportList", method = RequestMethod.GET)
    public ResponseEntity getReportList(@CustomReportTypeValid
                                        @RequestParam Integer reportType,
                                        @RequestParam(defaultValue = "0") String projectOrTaskId,
                                        @RequestParam(defaultValue = "0") String creatorId,
                                        @RequestParam(defaultValue = "0") String startDate,
                                        @RequestParam(defaultValue = "0") String endDate,
                                        @RequestParam(defaultValue = "0") String utcOffset) {

        if (utcOffset.equals("0"))
            utcOffset = getCurrentOffset();

        return reportService.getReportList(reportType, getPrincipalName(), Integer.parseInt(projectOrTaskId), Integer.parseInt(creatorId),
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
