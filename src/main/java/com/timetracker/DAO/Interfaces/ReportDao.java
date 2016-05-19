package com.timetracker.DAO.Interfaces;


import com.timetracker.Entities.Report;

import java.util.List;

public interface ReportDao {
    void createReport(Report newReport);
    List<Report> getTaskAllReportList(int taskId);
}
