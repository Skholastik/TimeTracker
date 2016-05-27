package com.timetracker.Entities.DTO;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class ReporterDTO {
    private int id;
    private String name;
    private String taskElapsedTime;
    Set<ReportDTO> reportList;


    public void addTaskElapsedTime(LocalTime time) {

        String terms[]=new String[2];
        terms[0]=taskElapsedTime;
        terms[1]=time.toString();

        int sum = 0;

        for( String hhmm : terms ){
            String[] split = hhmm.split( ":", 2 );
            int mins = Integer.valueOf(split[ 0 ]) * 60 + Integer.valueOf( split[ 1 ] );
            sum += mins;
        }

       taskElapsedTime = (int)Math.floor(sum/60) + ":" + ( sum % 60 );

    }

    public void addReport(ReportDTO report) {

        if (reportList == null)
            reportList = new HashSet<>();

        reportList.add(report);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskElapsedTime() {
        return taskElapsedTime;
    }

    public void setTaskElapsedTime(String taskElapsedTime) {
        this.taskElapsedTime = taskElapsedTime;
    }

    public Set<ReportDTO> getReportList() {
        return reportList;
    }

    public void setReportList(Set<ReportDTO> reportList) {
        this.reportList = reportList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReporterDTO)) return false;

        ReporterDTO that = (ReporterDTO) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
