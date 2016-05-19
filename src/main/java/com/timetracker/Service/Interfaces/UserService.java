package com.timetracker.Service.Interfaces;

import com.timetracker.Entities.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    User findById(int id);
    User findByUserName(String userName);
    ResponseEntity getUserList();
}
