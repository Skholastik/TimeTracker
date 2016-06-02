package com.timetracker.DAO.Interfaces;

import com.timetracker.Entities.Task;

import java.util.List;

public interface TaskDao {
    void createTask(Task newTask);
    Task getTaskById(int taskId);
    List<Task> getProjectHighTaskList(int projectId);
    List<Task> getCreatedTaskList(String creatorUserName);
}
