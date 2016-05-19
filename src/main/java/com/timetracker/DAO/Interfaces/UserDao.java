package com.timetracker.DAO.Interfaces;

import com.timetracker.Entities.User;

import java.util.List;

public interface UserDao {

    User findById(int id);
    User findByUserName(String userName);
    List<User> getUserList();

}