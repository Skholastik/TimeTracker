package com.timetracker.DAO.Implementation;

import com.timetracker.DAO.Interfaces.ProjectDao;
import com.timetracker.Entities.Project;
import com.timetracker.Entities.ProjectParticipants;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository("projectDaoImpl")
public class ProjectDaoImpl implements ProjectDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createProject(Project newProject) {
        entityManager.persist(newProject);
    }

    @Override
    public void addProjectParticipant(ProjectParticipants projectParticipants) {
        entityManager.persist(projectParticipants);
    }

    @Override
    public Project getProjectById(int id) {
        return entityManager.find(Project.class, id);
    }

    @Override
    public List<Project> getUserProjectListByStatus(String userName,String status){
        return entityManager.createQuery("SELECT p.project FROM ProjectParticipants p " +
                "WHERE p.participant.userName=:userName AND p.project.status=:status ",Project.class)
                .setParameter("userName",userName)
                .setParameter("status",status)
                .getResultList();
    }

    @Override
    public List<Project> getCreatedProjectList(String creatorUserName) {
        return entityManager.createQuery("FROM Project p WHERE p.creator.userName=:creatorUserName",Project.class)
                .setParameter("creatorUserName",creatorUserName)
                .getResultList();
    }

}
