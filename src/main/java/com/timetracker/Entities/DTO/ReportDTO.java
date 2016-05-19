package com.timetracker.Entities.DTO;


import com.timetracker.Service.AncillaryServices.CustomAnnotation.CustomIDValid;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.groups.Default;

public class ReportDTO {

    @CustomIDValid(groups = {Default.class})
    Integer id;

    @NotBlank(message = "Необходимо заполнить отчет",
            groups = {CreateReport.class, Default.class})
    String report;

    @NotBlank(message = "Необходимо заполнить дату отчета",
            groups = {CreateReport.class, Default.class})
    String workTime;

    @NotBlank(message = "Необходимо указать потраченное время",
            groups = {CreateReport.class, Default.class})
    String workDate;

    String creationDateTime;

    UserDTO creator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }


    public interface CreateReport {
    }
}