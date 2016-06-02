package com.timetracker.Service.Implementation;

import com.timetracker.DAO.Interfaces.ReportDao;
import com.timetracker.DAO.Interfaces.TaskDao;
import com.timetracker.DAO.Interfaces.UserDao;
import com.timetracker.Entities.DTO.ProjectDTO;
import com.timetracker.Entities.DTO.ReportDTO;
import com.timetracker.Entities.DTO.ReporterDTO;
import com.timetracker.Entities.DTO.TaskDTO;
import com.timetracker.Entities.Report;
import com.timetracker.Entities.Task;
import com.timetracker.Entities.User;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import com.timetracker.Service.AncillaryServices.WordProcessor;
import com.timetracker.Service.Interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportDao reportDao;

    @Autowired
    TaskDao taskDao;

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity createTaskReport(String report, String workTime, String workDate,
                                           int taskId, String userUtcOffset, String creatorUserName) {

        String formattedReport = WordProcessor.prepareWordToDB(report);
        Task ancestorTask = taskDao.getTaskById(taskId);
        User reporter = userDao.findByUserName(creatorUserName);
        Report newReport = new Report();
        newReport.setReport(formattedReport);
        newReport.setWorkTime(LocalTime.parse(workTime));
        newReport.setWorkDate(LocalDate.parse(workDate));

        ZonedDateTime serverDateTime = Instant.now().atZone(ZoneId.systemDefault());
        newReport.setCreationDateTime(serverDateTime);

        reportDao.createReport(newReport);

        newReport.setReporter(reporter);
        newReport.setAncestorTask(ancestorTask);
        newReport.setAncestorProject(ancestorTask.getAncestorProject());

        ReporterDTO reporterDTO=new ReporterDTO();
        reporterDTO.setId(reporter.getId());
        reporterDTO.setName(reporter.getUserName());
        ReportDTO reportDTO = reportToDTO(newReport, userUtcOffset);
        reporterDTO.addReport(reportDTO);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("reporter", reporterDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getTaskReporterList(int taskId, String userUtcOffset) {

        List<User> reporterList = reportDao.getTaskReporterList(taskId);
        List<ReporterDTO> reporterDTOList = userListToReporterDTO(reporterList, userUtcOffset);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("reporterList", reporterDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getReportList(Integer reportType,String creatorUserName, int projectOrTaskId, int creatorId, String startDate, String endDate, String userUtcOffset) {
        List<Report> reportList = reportDao.getReportList(reportType,creatorUserName, projectOrTaskId, creatorId, startDate, endDate);

        Set<ProjectDTO> reportDTOList = prepareDetailedReport(reportList, userUtcOffset);
        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("reportProjectList", reportDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public List<ReportDTO> reportListToDTO(List<Report> reportList, String userUtcOffset) {
        List<ReportDTO> reportDTOList = new ArrayList<>();

        for (Report report : reportList)
            reportDTOList.add(reportToDTO(report, userUtcOffset));

        return reportDTOList;
    }

    public ReportDTO reportToDTO(Report report, String userUtcOffset) {
        ReportDTO newReportDTO = new ReportDTO();
        newReportDTO.setId(report.getId());
        newReportDTO.setReport(report.getReport());
        newReportDTO.setWorkTime(report.getWorkTime().toString());
        newReportDTO.setWorkDate(report.getWorkDate().toString());

        newReportDTO.setCreationDateTime(report.getCreationDateTime()
                .withZoneSameInstant(ZoneOffset.of(userUtcOffset))
                .toLocalDateTime().toString());

        return newReportDTO;
    }

    public List<ReporterDTO> userListToReporterDTO(List<User> userList, String userUtcOffset) {
        List<ReporterDTO> reporterDTOList = new ArrayList<>();

        for (User reporter : userList) {
            ReporterDTO newReporter = new ReporterDTO();
            newReporter.setId(reporter.getId());
            newReporter.setName(reporter.getUserName());

            for (Report report : reporter.getCreatedReportList())
                newReporter.addReport(reportToDTO(report, userUtcOffset));

            reporterDTOList.add(newReporter);
        }
        return reporterDTOList;
    }

    /**
     * Заполняет полную цепочку от отчета до проекта, на основе входящего массива отчетов.
     * Такой подход необходим из-за особенностей реализации на клиенте.
     */

    public Set<ProjectDTO> prepareDetailedReport(List<Report> reportList, String userUtcOffset) {
        Set<ProjectDTO> reportProjectList = new HashSet<>();

        for (Report report : reportList) {

            ProjectDTO newProject = new ProjectDTO();
            newProject.setId(report.getAncestorProject().getId());

            if (reportProjectList.add(newProject)) {
                newProject.setName(report.getAncestorProject().getName());

                TaskDTO newTask = new TaskDTO();
                newTask.setId(report.getAncestorTask().getId());
                newTask.setName(report.getAncestorTask().getName());

                ReporterDTO newReporter = new ReporterDTO();

                newReporter.setId(report.getReporter().getId());
                newReporter.setName(report.getReporter().getUserName());
                newReporter.setTaskElapsedTime(report.getWorkTime().toString());

                ReportDTO newReport = reportToDTO(report, userUtcOffset);

                newReporter.addReport(newReport);
                newTask.addReporter(newReporter);
                newProject.addTask(newTask);

            } else {

                for (ProjectDTO project : reportProjectList) {

                    if (project.getId() == report.getAncestorProject().getId()) {
                        TaskDTO newTask = new TaskDTO();
                        newTask.setId(report.getAncestorTask().getId());

                        if (project.getTaskList().add(newTask)) {
                            newTask.setName(report.getAncestorTask().getName());

                            ReporterDTO newReporter = new ReporterDTO();
                            newReporter.setId(report.getReporter().getId());
                            newReporter.setName(report.getReporter().getUserName());
                            newReporter.setTaskElapsedTime(report.getWorkTime().toString());

                            ReportDTO newReport = reportToDTO(report, userUtcOffset);

                            newReporter.addReport(newReport);
                            newTask.addReporter(newReporter);
                            project.addTask(newTask);

                        } else {

                            for (TaskDTO task : project.getTaskList()) {

                                if (task.getId() == report.getAncestorTask().getId()) {

                                    ReporterDTO newReporter = new ReporterDTO();
                                    newReporter.setId(report.getReporter().getId());

                                    if (task.getReporterList().add(newReporter)) {
                                        newReporter.setName(report.getReporter().getUserName());
                                        newReporter.setTaskElapsedTime(report.getWorkTime().toString());

                                        ReportDTO newReport = reportToDTO(report, userUtcOffset);

                                        newReporter.addReport(newReport);
                                        task.addReporter(newReporter);
                                    } else {

                                        for (ReporterDTO reporter : task.getReporterList()) {

                                            if (reporter.getId() == report.getReporter().getId()) {
                                                reporter.addTaskElapsedTime(report.getWorkTime());
                                                ReportDTO newReportDTO = reportToDTO(report, userUtcOffset);

                                                reporter.addReport(newReportDTO);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return reportProjectList;
    }

}
