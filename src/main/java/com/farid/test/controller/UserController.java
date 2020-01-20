package com.farid.test.controller;

import com.farid.test.dao.UserDao;
import com.farid.test.model.UserResponse;
import com.farid.test.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value="api/users")
public class UserController {
    @Autowired
    UserDao userDao;

    @RequestMapping(value="signup", method= RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public UserResponse saveUser(@RequestBody User request) {
        UserResponse ur = new UserResponse();
        try {
                boolean result = userDao.save(request);
                if(result) {
                    ur.email = request.getEmail();
                    ur.token = "Success";
                    ur.username = request.getUsername();
                }

        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("User Service - Save Data User",e);
        }
        return ur;
    }
}
