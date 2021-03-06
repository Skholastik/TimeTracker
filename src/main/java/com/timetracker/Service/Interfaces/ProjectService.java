package com.timetracker.Service.Interfaces;

import com.timetracker.Entities.Project;
import com.timetracker.Entities.User;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface ProjectService {
    ResponseEntity getUserProjectListByStatus(String userName, String status, String userUtcOffset);

    ResponseEntity getCreatedProjectList(String creatorUserName, String userUtcOffset);

    ResponseEntity createProject(String projectName, String userUtcOffset, String creatorUserName);

    ResponseEntity checkNameInDB(String projectName, String userName);

    ResponseEntity setName(int projectId, String name, String creatorUserName);

    ResponseEntity setDescription(int projectId, String description, String creatorUserName);

    ResponseEntity setStatus(int projectId, String projectStatus, String creatorUserName);

    ResponseEntity<ResponseMessage> checkAccessToChangeProject(int projectId, String userName);

    void participateInProject(User user, Project project);

    boolean isProjectExist(String projectName, User userName);

}
