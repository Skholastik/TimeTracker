package com.timetracker.DAO.Implementation;


import com.timetracker.DAO.Interfaces.UserDao;
import com.timetracker.Entities.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Iterator;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public User findById(int id) {
        return entityManager.find(User.class, id);
    }

    public User findByUserName(String userName) {
        User user = null;
        List<User> list = entityManager.createQuery("FROM User u WHERE u.userName=:userName", User.class)
                .setParameter("userName", userName)
                .getResultList();
        Iterator iterator = list.iterator();

        if (iterator.hasNext())
            user = (User) iterator.next();

        return user;

    }

    @Override
    public List<User> getUserList() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public List<User> getProjectParticipantsList(int projectId, String ownerName) {

        if (projectId != 0)
            return entityManager.createQuery("SELECT p.participant FROM ProjectParticipants p WHERE p.project.id=:id ", User.class)
                    .setParameter("id", projectId).getResultList();

        else
            return entityManager.createQuery("SELECT DISTINCT p.participant FROM ProjectParticipants p WHERE p.project.creator.userName=:name ", User.class)
                    .setParameter("name", ownerName).getResultList();
    }

    @Override
    public List<User> getTaskParticipantsList(int taskId) {

            return entityManager.createQuery("SELECT r.creator FROM Report r WHERE r.ancestorTask.id=:id ", User.class)
                    .setParameter("id", taskId).getResultList();
    }

}
