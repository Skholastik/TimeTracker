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
        if (iterator.hasNext()) {
            user = (User) iterator.next();

        }
        return user;

    }

    @Override
    public List<User> getUserList() {
        return entityManager.createQuery("FROM User",User.class).getResultList();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
