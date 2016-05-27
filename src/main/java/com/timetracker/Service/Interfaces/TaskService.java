package com.timetracker.Service.Interfaces;

import com.timetracker.Service.AncillaryServices.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    ResponseEntity createTask(String taskName, int ancestorProjectId, String userUtcOffset, String creatorUserName);
    ResponseEntity addTaskExecutor(int taskId, int userId, String creatorUserName);
    ResponseEntity getProjectHighTaskList(int projectId, String utcOffset);
    ResponseEntity getCreatedTaskList(String creatorUserName,String utcOffset);
    ResponseEntity checkNameInDB(int projectId, String taskName);
    ResponseEntity setName(int taskId, String name, String creatorUserName);
    ResponseEntity setDescription(int taskId,String description,String creatorUserName);
    ResponseEntity setStatus(int taskId,String taskStatus,String creatorUserName);
    ResponseEntity<ResponseMessage> checkLowLevelAuthorities(int taskId, String userName);
    ResponseEntity<ResponseMessage> checkHighLevelAuthorities(int taskId, String userName);

    boolean isTaskExist(String taskName, int projectId);

}
