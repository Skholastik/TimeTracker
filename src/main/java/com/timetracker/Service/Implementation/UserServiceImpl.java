package com.timetracker.Service.Implementation;


import com.timetracker.DAO.Interfaces.UserDao;
import com.timetracker.Entities.DTO.UserDTO;
import com.timetracker.Entities.State;
import com.timetracker.Entities.User;
import com.timetracker.Service.AncillaryServices.ResponseMessage;
import com.timetracker.Service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User findById(int id) {
        return userDao.findById(id);
    }

    public User findByUserName(String userName) {
        return userDao.findByUserName(userName);
    }

    @Override
    public ResponseEntity checkAccess(String userName) {

        if (userName.equals("anonymousUser")) {
            ResponseMessage responseMessage = new ResponseMessage(false, "");
            responseMessage.addResponseObject("errorMessage", "Ошибка авторизации: для совершения данного действия вам необходимо авторизоваться");
            return new ResponseEntity<>(responseMessage, HttpStatus.UNAUTHORIZED);
        }
        else {
            UserDTO user=new UserDTO();
            user.setUserName(userName);
            ResponseMessage responseMessage = new ResponseMessage(true, "");
            responseMessage.addResponseObject("user", user);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }


    }

    @Override
    public ResponseEntity getUserList() {
        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("userList",
                transformUserListToDTO(userDao.getUserList()));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getParticipantProjectUserList(int projectId, String ownerName) {
        List<User> projectParticipantsList = userDao.getProjectParticipantsList(projectId, ownerName);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("userList",
                transformUserListToDTO(projectParticipantsList));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity getParticipantTaskUserList(int taskId) {
        List<User> projectParticipantsList = userDao.getTaskParticipantsList(taskId);

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        responseMessage.addResponseObject("userList",
                transformUserListToDTO(projectParticipantsList));

        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity signUp(String userName, String email, String password) {
        User newUser=new User();
        newUser.setUserName(userName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setState(State.ACTIVE.getState());
        userDao.createUser(newUser);
        newUser.setRole(userDao.getUserRole());

        ResponseMessage responseMessage = new ResponseMessage(true, "");
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public List<UserDTO> transformUserListToDTO(List<User> userList) {
        List<UserDTO> newUserDTOList = new ArrayList<>();

        for (User user : userList) {
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setUserName(user.getUserName());
            newUserDTO.setId(user.getId());
            newUserDTOList.add(newUserDTO);
        }

        return newUserDTOList;
    }

}
