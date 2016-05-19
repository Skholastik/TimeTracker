package com.timetracker.Service.Interfaces;

import com.timetracker.Service.AncillaryServices.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    ResponseEntity createTask(String taskName, int ancestorProjectId, String userUtcOffset, String userName);
    ResponseEntity addTaskExecutor(int taskId, int userId, String owner);
    ResponseEntity getProjectHighTaskList(int projectId, String utcOffset);
    ResponseEntity getCreatedTaskList(String creatorName,String utcOffset);
    ResponseEntity checkNameInDB(int projectId, String taskName);
    ResponseEntity setName(int taskId, String name, String userName);
    ResponseEntity setDescription(int taskId,String description,String userName);
    ResponseEntity setStatus(int taskId,String taskStatus,String userName);
    ResponseEntity<ResponseMessage> checkLowLevelAuthorities(int taskId, String userName);
    ResponseEntity<ResponseMessage> checkHighLevelAuthorities(int taskId, String userName);

    boolean isTaskExist(String taskName, int projectId);

}
