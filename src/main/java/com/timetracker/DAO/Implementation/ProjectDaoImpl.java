package com.timetracker.DAO.Implementation;

import com.timetracker.DAO.Interfaces.ProjectDao;
import com.timetracker.Entities.Project;
import com.timetracker.Entities.ProjectParticipants;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


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

}
