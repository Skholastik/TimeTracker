package com.timetracker.Service.Interfaces;

import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity createTaskReport(String report, String workTime, String workDate,
                                    int taskId, String userUtcOffset, String username);
    ResponseEntity getTaskAllReportList(int taskId,String userUtcOffset);
}
