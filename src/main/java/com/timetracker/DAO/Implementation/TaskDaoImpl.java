package com.timetracker.DAO.Implementation;

import com.timetracker.DAO.Interfaces.TaskDao;
import com.timetracker.Entities.Task;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository("taskDaoImpl")
public class TaskDaoImpl implements TaskDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createTask(Task newTask) {
        entityManager.persist(newTask);
    }

    @Override
    public Task getTaskById(int taskId) {
        return entityManager.find(Task.class,taskId);
    }

    @Override
    public List<Task> getProjectHighTaskList(int projectId) {

        return entityManager.createQuery("FROM Task t WHERE t.ancestorProject.id=:projectId AND t.ancestorTask.id=null ", Task.class)
                .setParameter("projectId", projectId).getResultList();
    }
}
