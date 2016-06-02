package com.timetracker.Service.Interfaces;

import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity createTaskReport(String report, String workTime, String workDate,
                                    int taskId, String userUtcOffset, String reporterUserName);

    ResponseEntity getTaskReporterList(int taskId, String userUtcOffset);

    ResponseEntity getReportList(Integer reportType,String creatorUserName,int projectOrTaskId, int creatorId, String startDate,
                                                String endDate,String userUtcOffset);


}
