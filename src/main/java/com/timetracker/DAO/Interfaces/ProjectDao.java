package com.timetracker.DAO.Interfaces;


import com.timetracker.Entities.Project;
import com.timetracker.Entities.ProjectParticipants;

import java.util.List;

public interface ProjectDao {

    void createProject(Project newProject);
    void addProjectParticipant(ProjectParticipants projectParticipants);
    Project getProjectById(int id);
    List<Project> getUserProjectListByStatus(String userName, String status);
    List<Project> getCreatedProjectList(String creatorUserName);


}
