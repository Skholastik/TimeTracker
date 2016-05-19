package com.timetracker.DAO.Interfaces;


import com.timetracker.Entities.Project;
import com.timetracker.Entities.ProjectParticipants;

import java.time.LocalDateTime;

public interface ProjectDao {

    void createProject(Project newProject);
    void addProjectParticipant(ProjectParticipants projectParticipants);
    Project getProjectById(int id);


}
