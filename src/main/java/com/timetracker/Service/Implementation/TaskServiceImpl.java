package com.timetracker.Service.Implementation;

import com.timetracker.DAO.Interfaces.ProjectDao;
import com.timetracker.DAO.Interfaces.TaskDao;
import com.timetracker.DAO.Interfaces.UserDao;
import com.timetracker.Entities.DTO.TaskDTO;
import com.timetracker.Entities.DTO.UserDTO;
import com.timetracker.Entities.Project;
import com.timetracker.Entities.Status;
import com.timetracker.Entities.Task;
import com.timetracker.Entities.User;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import com.timetracker.Service.AncillaryServices.WordProcessor;
import com.timetracker.Service.Interfaces.ProjectService;
import com.timetracker.Service.Interfaces.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskDao taskDao;

    @Autowired
    ProjectDao projectDao;

    @Autowired
    UserDao userDao;

    @Autowired
    ProjectService projectService;

    @Override
    public ResponseEntity createTask(String taskName, int ancestorProjectId, String userUtcOffset, String creatorUserName) {
        String formattedTaskName = WordProcessor.prepareWordToDB(taskName);
        User creator = userDao.findByUserName(creatorUserName);

        if (isTaskExist(formattedTaskName, ancestorProjectId)) {
            ResponseMessage responseMessage = new ResponseMessage(false, "Задание с названием: " + "'"
                    + formattedTaskName + "'" + " уже есть в проекте");
            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_ACCEPTABLE);
        }

        Task newTask = new Task();
        newTask.setName(formattedTaskName);
        ZonedDateTime serverDateTime = Instant.now().atZone(ZoneId.systemDefault());
        newTask.setCreationDateTime(serverDateTime);
        newTask.setStatus(Status.NEW.getStatus());

        taskDao.createTask(newTask);

        Project ancestor = projectDao.getProjectById(ancestorProjectId);

        newTask.setAncestorProject(ancestor);
        newTask.setCreator(creator);

        if (newTask.getAncestorTask() == null)
            newTask.setPath(ancestor.getId() + "." + newTask.getId());
        else
            newTask.setPath(newTask.getAncestorTask().getPath() + "." + newTask.getId());

        TaskDTO taskDTO = taskToDTO(newTask,userUtcOffset);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("task", taskDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity addTaskExecutor(int taskId, int userId, String creatorUserName) {
        ResponseEntity<ResponseMessage> responseEntity = checkHighLevelAuthorities(taskId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        User executor = userDao.findById(userId);
        Task task = taskDao.getTaskById(taskId);

        if (task.getExecutor() != null)
            return new ResponseEntity<>(new ResponseMessage(false, "У задачи: " + "'"
                    + task.getName() + "'" + ", уже есть исполнитель"), HttpStatus.FORBIDDEN);

        task.setExecutor(executor);
        projectService.participateInProject(executor, task.getAncestorProject());

        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setId(executor.getId());
        newUserDTO.setName(executor.getUserName());

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("executor", newUserDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getProjectHighTaskList(int projectId, String utcOffset) {
        List<Task> highTaskList = taskDao.getProjectHighTaskList(projectId);
        List<TaskDTO> taskDTOList = taskListToDTOWithChangeOffset(highTaskList, utcOffset);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("taskList", taskDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getCreatedTaskList(String creatorUserName, String utcOffset) {
        User owner = userDao.findByUserName(creatorUserName);
        List<Task> createdTaskList = owner.getCreatedTaskList();
        createdTaskList.size();

        List<TaskDTO> taskDTOList = taskListToDTOWithChangeOffset(createdTaskList, utcOffset);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("taskList", taskDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity checkNameInDB(int projectId, String taskName) {
        String formattedTaskName = WordProcessor.prepareWordToDB(taskName);

        if (isTaskExist(formattedTaskName, projectId)) {
            ResponseMessage responseMessage = new ResponseMessage(false, "Задание с названием: " + "'"
                    + taskName + "'" + " имеется в данном проекте");
            return new ResponseEntity<>(responseMessage, HttpStatus.FOUND);
        }

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity setName(int taskId, String name, String creatorUserName) {
        Task task = taskDao.getTaskById(taskId);
        ResponseEntity<ResponseMessage> responseEntity = checkLowLevelAuthorities(taskId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        task.setName(name);
        return responseEntity;

    }

    @Override
    public ResponseEntity setDescription(int taskId, String description, String creatorUserName) {
        Task task = taskDao.getTaskById(taskId);
        ResponseEntity<ResponseMessage> responseEntity = checkLowLevelAuthorities(taskId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        task.setDescription(description);
        return responseEntity;
    }

    @Override
    public ResponseEntity setStatus(int taskId, String taskStatus, String creatorUserName) {
        Task task = taskDao.getTaskById(taskId);
        ResponseEntity<ResponseMessage> responseEntity = checkLowLevelAuthorities(taskId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        task.setStatus(taskStatus);
        return responseEntity;

    }

    @Override
    public ResponseEntity<ResponseMessage> checkLowLevelAuthorities(int taskId, String userName) {
        Task task = taskDao.getTaskById(taskId);
        ResponseMessage responseMessage = null;
        String executorName = task.getExecutor() == null ? " " : task.getExecutor().getUserName();

        if (!task.getCreator().getUserName().equals(userName) || executorName.equals(userName)) {
            responseMessage = new ResponseMessage(false, "Вы не создатель, а также не исполнитель задачи: " + "'"
                    + task.getName() + "'" + ", поэтому у вас нет прав для внесения изменений в нее");
            return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
        } else {
            responseMessage = new ResponseMessage(true, "");
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> checkHighLevelAuthorities(int taskId, String userName) {
        Task task = taskDao.getTaskById(taskId);
        ResponseMessage responseMessage = null;

        if (!task.getCreator().getUserName().equals(userName)) {
            responseMessage = new ResponseMessage(false, "Вы не создатель задачи: " + "'"
                    + task.getName() + "'" + ", поэтому у вас нет прав для внесения изменений в нее");
            return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
        } else {
            responseMessage = new ResponseMessage(true, "");
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
    }

    @Override
    public boolean isTaskExist(String taskName, int projectId) {
        Project project = projectDao.getProjectById(projectId);
        List<Task> taskList = project.getTaskList();
        taskList.size();

        for (Task task : taskList)
            if (task.getName().equals(taskName))
                return true;
        return false;
    }

    public List<TaskDTO> taskListToDTOWithChangeOffset(List<Task> taskList, String userUtcOffset) {
        List<TaskDTO> taskDTOList = new ArrayList<>();

        for (Task task : taskList)
            taskDTOList.add(taskToDTO(task,userUtcOffset));

        return taskDTOList;
    }

    public TaskDTO taskToDTO(Task task, String userUtcOffset) {
        TaskDTO newTaskDTO = new TaskDTO();
        newTaskDTO.setId(task.getId());
        newTaskDTO.setName(task.getName());
        newTaskDTO.setAncestorProjectId(task.getAncestorProject().getId());
        newTaskDTO.setCreationDateTime(task.getCreationDateTime()
                .withZoneSameInstant(ZoneOffset.of(userUtcOffset))
                .toLocalDateTime().toString());
        newTaskDTO.setStatus(task.getStatus());
        newTaskDTO.setCreator(new UserDTO(task.getCreator().getUserName()));

        if (task.getExecutor() != null) {
            newTaskDTO.setExecutor(new UserDTO(task.getExecutor().getUserName()));
        }

        if (task.getDescription() != null)
            newTaskDTO.setDescription(task.getDescription());

        if (task.getPlannedEndDateTime() != null)
            newTaskDTO.setPlannedEndDateTime(task.getPlannedEndDateTime()
                    .withZoneSameInstant(ZoneOffset.of(userUtcOffset))
                    .toLocalDateTime().toString());

        if (task.getActualEndDateTime() != null)
            newTaskDTO.setActualEndDateTime(task.getActualEndDateTime()
                    .withZoneSameInstant(ZoneOffset.of(userUtcOffset))
                    .toLocalDateTime().toString());

        return newTaskDTO;
    }


}
