package com.timetracker.Service.Implementation;

import com.timetracker.DAO.Interfaces.ProjectDao;
import com.timetracker.DAO.Interfaces.UserDao;
import com.timetracker.Entities.DTO.ProjectDTO;
import com.timetracker.Entities.DTO.UserDTO;
import com.timetracker.Entities.Project;
import com.timetracker.Entities.ProjectParticipants;
import com.timetracker.Entities.Status;
import com.timetracker.Entities.User;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import com.timetracker.Service.AncillaryServices.WordProcessor;
import com.timetracker.Service.Interfaces.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    ProjectDao projectDao;

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity getUserActiveProjectList(String userName, String userUtcOffset) {
        User user = userDao.findByUserName(userName);
        List<ProjectParticipants> projectParticipantsList = user.getTakingPartProjectList();
        projectParticipantsList.size();
        List<Project> projectList = new ArrayList<>();

        for (ProjectParticipants takingPartProject : projectParticipantsList) {
            if (takingPartProject.getProject().getStatus().equals(Status.ACTIVE.getStatus()))
                projectList.add(takingPartProject.getProject());
        }

        List<ProjectDTO> projectDTOList = projectListToDTOWithChangeOffset(projectList, userUtcOffset);
        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("projectList", projectDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getCreatedProjectList(String userName, String userUtcOffset) {
        User user = userDao.findByUserName(userName);
        List<Project> createdProjectList = user.getCreatedProjectList();
        createdProjectList.size();

        List<ProjectDTO> projectDTOList = projectListToDTOWithChangeOffset(createdProjectList, userUtcOffset);
        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("projectList", projectDTOList);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity createProject(String projectName, String userUtcOffset, String creatorUserName) {
        String formattedProjectName = WordProcessor.prepareWordToDB(projectName);
        User creator = userDao.findByUserName(creatorUserName);

        if (isProjectExist(formattedProjectName, creator)) {
            ResponseMessage responseMessage = new ResponseMessage(false, "Проект с названием: " + "'"
                    + formattedProjectName + "'" + " имеется у данного пользователя");
            return new ResponseEntity<>(responseMessage, HttpStatus.NOT_ACCEPTABLE);
        }

        Project newProject = new Project();

        newProject.setName(formattedProjectName);
        ZonedDateTime serverDateTime = Instant.now().atZone(ZoneId.systemDefault());
        newProject.setCreationDateTime(serverDateTime);
        newProject.setCreator(creator);
        newProject.setStatus(Status.ACTIVE.getStatus());

        projectDao.createProject(newProject);

        participateInProject(creator, newProject);

        ProjectDTO projectDTO = projectToDTO(newProject, userUtcOffset);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("project", projectDTO);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity checkNameInDB(String projectName, String userName) {
        String formattedProjectName = WordProcessor.prepareWordToDB(projectName);
        User owner = userDao.findByUserName(userName);

        if (isProjectExist(formattedProjectName, owner)) {
            ResponseMessage responseMessage = new ResponseMessage(false, "Проект с названием: " + "'"
                    + projectName + "'" + " имеется у данного пользователя");
            return new ResponseEntity<>(responseMessage, HttpStatus.FOUND);
        }

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity setName(int projectId, String name, String creatorUserName) {
        Project project = projectDao.getProjectById(projectId);
        ResponseEntity<ResponseMessage> responseEntity = checkAccessToChangeProject(projectId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        project.setName(name);
        return responseEntity;
    }

    @Override
    public ResponseEntity setDescription(int projectId, String description, String creatorUserName) {
        Project project = projectDao.getProjectById(projectId);
        ResponseEntity<ResponseMessage> responseEntity = checkAccessToChangeProject(projectId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        project.setDescription(description);
        return responseEntity;
    }

    @Override
    public ResponseEntity setStatus(int projectId, String projectStatus, String creatorUserName) {
        Project project = projectDao.getProjectById(projectId);
        ResponseEntity<ResponseMessage> responseEntity = checkAccessToChangeProject(projectId, creatorUserName);

        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN)
            return responseEntity;

        project.setStatus(projectStatus);
        return responseEntity;

    }

    public ResponseEntity<ResponseMessage> checkAccessToChangeProject(int projectId, String userName) {
        Project project = projectDao.getProjectById(projectId);
        ResponseMessage responseMessage = null;

        if (!project.getCreator().getUserName().equals(userName)) {
            responseMessage = new ResponseMessage(false, "Вы не владелец проекта: " + "'"
                    + project.getName() + "'" + ", поэтому у вас нет прав для внесения изменений в проект");
            return new ResponseEntity<>(responseMessage, HttpStatus.FORBIDDEN);
        } else {
            responseMessage = new ResponseMessage(true, "");
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
    }


    /**
     * Осуществляет проверку в базе данных на наличие у пользователя проекта
     * с {@param projectName} названием. Возвращает true, если проект с данным
     * названием существует, false, если нет
     */

    @Override
    public boolean isProjectExist(String projectName, User userName) {
        List<Project> projectList = userName.getCreatedProjectList();
        projectList.size();

        for (Project project : projectList)
            if (project.getName().equals(projectName))
                return true;

        return false;
    }

    @Override
    public void participateInProject(User user, Project project) {
        ProjectParticipants projectParticipants = new ProjectParticipants();
        projectParticipants.setStatus(Status.ACTIVE.getStatus());
        projectDao.addProjectParticipant(projectParticipants);
        projectParticipants.setParticipant(user);
        projectParticipants.setProject(project);
    }

    public List<ProjectDTO> projectListToDTOWithChangeOffset(List<Project> projectList, String userUtcOffset) {
        List<ProjectDTO> projectDTOList = new ArrayList<>();

        for (Project project : projectList)
            projectDTOList.add(projectToDTO(project, userUtcOffset));

        return projectDTOList;
    }

    public ProjectDTO projectToDTO(Project project, String userUtcOffset) {
        ProjectDTO newProjectDTO = new ProjectDTO();

        newProjectDTO.setId(project.getId());
        newProjectDTO.setName(project.getName());
        newProjectDTO.setStatus(project.getStatus());
        newProjectDTO.setCreator(new UserDTO(project.getCreator().getUserName()));

        newProjectDTO.setCreationDateTime(project.getCreationDateTime()
                .withZoneSameInstant(ZoneOffset.of(userUtcOffset))
                .toLocalDateTime().toString());

        if (project.getDescription() != null)
            newProjectDTO.setDescription(project.getDescription());

        return newProjectDTO;
    }


}
