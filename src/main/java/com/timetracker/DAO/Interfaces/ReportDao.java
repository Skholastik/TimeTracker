package com.timetracker.DAO.Interfaces;


import com.timetracker.Entities.Report;
import com.timetracker.Entities.User;

import java.util.List;

public interface ReportDao {
    void createReport(Report newReport);

    public List<User> getTaskReporterList(int taskId);

    List<Report> getReportList(Integer reportType,String ownerName, int projectOrTaskId, int reporterId,
                                      String startDate, String endDate);

}
