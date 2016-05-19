package com.timetracker.Service.Interfaces;

import com.timetracker.Entities.Project;
import com.timetracker.Entities.User;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface ProjectService {
    ResponseEntity getUserActiveProjectList(String userName,String userUtcOffset);
    ResponseEntity createProject(String projectName, String userUtcOffset, String userName);
    ResponseEntity checkNameInDB(String projectName, String userName );
    ResponseEntity setName(int projectId, String name, String userName);
    ResponseEntity setDescription(int projectId,String description,String userName);
    ResponseEntity setStatus(int projectId,String projectStatus,String userName);
    ResponseEntity<ResponseMessage> checkAccessToChangeProject(int projectId, String userName);
    void participateInProject(User user, Project project);

    boolean isProjectExist(String projectName, User userName);

}
