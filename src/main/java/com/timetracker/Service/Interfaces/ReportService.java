package com.timetracker.Service.Interfaces;

import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity createTaskReport(String report, String workTime, String workDate,
                                    int taskId, String userUtcOffset, String reporterUserName);

    ResponseEntity getTaskAllReportList(int taskId, String userUtcOffset);

    ResponseEntity getProjectReportList(String creatorUserName,int projectId, int creatorId, String startDate,
                                                String endDate,String userUtcOffset);

   ResponseEntity getTaskReportList(String creatorUserName,int taskId, int creatorId, String startDate,
                                                String endDate,String userUtcOffset);



}
