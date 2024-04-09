package com.alan.user.controller;

import com.alan.user.Constants.Constants;
import com.alan.user.entity.Users;
import com.alan.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Users> getUserFromToken(HttpServletRequest request) {
        return userService.getUserDetailsFromToken(request);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Users> getUser(@PathVariable long id) {
        return userService.getUserDetails(id);
    }

    @PostMapping(path = "/add")
    public ResponseEntity saveOrUpdateUser(@RequestBody Users users) {
        return userService.save(users);
    }

    @PutMapping(path = "/update")
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword) {
        return userService.update((Users) request.getAttribute(Constants.USER_OBJECT), oldPassword, newPassword);
    }

    @DeleteMapping(path = "/delete/user/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        return userService.deleteUser(id);
    }

}
