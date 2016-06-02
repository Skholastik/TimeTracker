package com.timetracker.Config.CustomSecure;

import com.timetracker.Entities.User;
import com.timetracker.Entities.UserRoles;
import com.timetracker.Service.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = service.findByUserName(username);

        if (user == null)
            throw new UsernameNotFoundException("User name not found");

        return buildUserFromUserEntity(user);
    }

    private org.springframework.security.core.userdetails.User buildUserFromUserEntity(User user) {

        String username = user.getUserName();
        String password = user.getPassword();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        UserRoles role = user.getRole();
        List<UserRoles> roles = new ArrayList<UserRoles>();
        roles.add(role);

        return new org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, roles);
    }

    public UserService getService() {
        return service;
    }

    public void setService(UserService service) {
        this.service = service;
    }

}
