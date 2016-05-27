package com.timetracker.DAO.Interfaces;


import com.timetracker.Entities.Report;

import java.util.List;

public interface ReportDao {
    void createReport(Report newReport);

    List<Report> getTaskAllReportList(int taskId);

    List<Report> getProjectReportList(String ownerName, int projectId, int creatorId,
                                      String startDate, String endDate);

    List<Report> getTaskReportList(String ownerName, int taskId, int creatorId,
                                      String startDate, String endDate);

}
