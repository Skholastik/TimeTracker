package com.timetracker.Service.Implementation;

import com.timetracker.DAO.Interfaces.ReportDao;
import com.timetracker.DAO.Interfaces.TaskDao;
import com.timetracker.DAO.Interfaces.UserDao;
import com.timetracker.Entities.DTO.ReportDTO;
import com.timetracker.Entities.DTO.UserDTO;
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
import java.util.List;

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
                                           int taskId, String userUtcOffset, String username) {

        String formattedReport = WordProcessor.prepareWordToDB(report);
        Task ancestorTask = taskDao.getTaskById(taskId);
        User creator=userDao.findByUserName(username);

        Report newReport = new Report();
        newReport.setReport(formattedReport);
        newReport.setWorkTime(LocalTime.parse(workTime));
        newReport.setWorkDate(LocalDate.parse(workDate));

        ZonedDateTime serverDateTime = Instant.now().atZone(ZoneId.systemDefault());
        newReport.setCreationDateTime(serverDateTime);

        reportDao.createReport(newReport);


        newReport.setCreator(creator);
        newReport.setAncestorTask(ancestorTask);
        newReport.setAncestorProject(ancestorTask.getAncestorProject());

        ReportDTO reportDTO = new ReportDTO();

        reportDTO.setId(newReport.getId());
        reportDTO.setReport(newReport.getReport());
        reportDTO.setWorkTime(newReport.getWorkTime().toString());
        reportDTO.setWorkDate(newReport.getWorkDate().toString());
        reportDTO.setCreator(new UserDTO(creator.getUserName()));

        ZonedDateTime userDateTime = serverDateTime.withZoneSameInstant(ZoneOffset.of(userUtcOffset));
        reportDTO.setCreationDateTime(userDateTime.toLocalDateTime()
                .toString());

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("report", reportDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity getTaskAllReportList(int taskId, String userUtcOffset) {
        List<Report> reportList=reportDao.getTaskAllReportList(taskId);
        List<ReportDTO> reportDTOList=reportListToDTOWithChangeOffset(reportList,userUtcOffset);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("reportList", reportDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public List<ReportDTO> reportListToDTOWithChangeOffset(List<Report> reportList, String userUtcOffset) {
        List<ReportDTO> reportDTOList = new ArrayList<>();

        for (Report report : reportList) {
            ReportDTO newReportDTO = new ReportDTO();
            newReportDTO.setId(report.getId());
            newReportDTO.setReport(report.getReport());
            newReportDTO.setWorkTime(report.getWorkTime().toString());
            newReportDTO.setWorkDate(report.getWorkDate().toString());
            newReportDTO.setCreator(new UserDTO(report.getCreator().getUserName()));

            newReportDTO.setCreationDateTime(report.getCreationDateTime()
                    .withZoneSameInstant(ZoneOffset.of(userUtcOffset))
                    .toLocalDateTime().toString());

            reportDTOList.add(newReportDTO);
        }
        return reportDTOList;
    }



}
