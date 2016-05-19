package com.timetracker.Entities.DTO;


public class ProjectNameAndClientUtcOffsetDTO {
    String projectName;
    String utcOffset;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }
}
