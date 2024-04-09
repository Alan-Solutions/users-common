package com.alan.user.service;

import com.alan.user.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity getUserDetails(Long id);

    public ResponseEntity getUserDetailsFromToken(HttpServletRequest request);

    public ResponseEntity save(Users user);
    public ResponseEntity update(Users user, String oldPassword, String newPassword);

    public ResponseEntity deleteUser(long id);

    public Users getUser(long id);

}
