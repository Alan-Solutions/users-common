package com.alan.user.service.impl;

import com.alan.user.Constants.Constants;
import com.alan.user.dao.UserRepository;
import com.alan.user.entity.Users;
import com.alan.user.service.UserService;
import com.alan.user.utils.AESUtils;
import com.alan.user.utils.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userServiceDao;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private AESUtils aesUtils;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    public ResponseEntity<?> getUserDetails(Long id) {
        return ResponseEntity.ok(getUser(id));
    }

    public ResponseEntity<?> getUserDetailsFromToken(HttpServletRequest request) {
        try {
            return ResponseEntity.ok(request.getAttribute(Constants.USER_OBJECT));
        } catch (Exception e) {
            logger.error("Error occurred while getting user details from token ", e);
            return ResponseEntity.status(500).body(e);
        }
    }

    public ResponseEntity<?> save(Users user) {
        try {
            return ResponseEntity.ok(saveUsers(user));
        } catch (Exception e) {
            logger.error("Error occurred while add or update user details ", e);
            return ResponseEntity.status(500).body(e);
        }
    }

    public ResponseEntity<?> update(Users user, String oldPassword, String newPassword) {
        logger.debug(user.toString());
        return updateUser(user, oldPassword, newPassword);
    }

    public ResponseEntity<String> deleteUser(long id) {
        try {
            userServiceDao.deleteById(id);
            return ResponseEntity.ok().body("Successfully deleted user");
        } catch (Exception e) {
            logger.error("Error occurred while deleting user ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User delete operation failed");
        }
    }

    private Users saveUsers(Users user) {
        setEncryptedPassword(user, base64DecodePassword(user.getPassword()));
        user = saveOrUpdateUser(user);
        user.setPassword("");
        return user;
    }

    private ResponseEntity<?> updateUser(Users user, String oldPassword, String newPassword) {
        Users actualUser = userServiceDao.findById(user.getId()).get();
        actualUser.setPassword(aesDecrypt(actualUser));
        if (!checkPassword(actualUser, oldPassword)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Old password not matching");
        }
        setEncryptedPassword(actualUser, base64DecodePassword(newPassword));
        logger.debug("Updated user");
        actualUser = saveOrUpdateUser(actualUser);
        actualUser.setPassword("");
        return ResponseEntity.ok().body(actualUser);
    }

    private boolean checkPassword(Users actualUser, String oldPassword) {
        String password = base64DecodePassword(oldPassword);
        return password.equals(actualUser.getPassword());
    }

    private Users saveOrUpdateUser(Users user) {
        return userServiceDao.save(user);
    }

    private String aesEncrypt(Users user) {
        return aesUtils.encrypt(user.getPassword().toCharArray(), String.valueOf(user.getCreatedTs()));
    }

    private String aesDecrypt(Users user) {
        return aesUtils.decrypt(user.getPassword().toCharArray(), String.valueOf(user.getCreatedTs()));
    }

    public Users getUser(long id) {
        try {
            Optional<Users> optUsers = userServiceDao.findById(id);
            Users user;
            if (optUsers.isPresent()) {
                user = optUsers.get();
                user.setPassword(aesDecrypt(user));
                logger.debug(String.format("Users.get()  %s", user));
                return user;
            }
            logger.debug(String.format("User id is empty %d", id));
            return new Users();
        } catch (Exception e) {
            logger.error("Error occurred while retriving user details ", e);
            return null;
        }
    }

    private String base64EncodePassword(String password) {
        return userUtils.base64Encode(password);
    }

    private String base64DecodePassword(String encodedPassword) {
        return userUtils.base64Decode(encodedPassword);
    }

    private void setEncryptedPassword(Users user, String password) {
        user.setCreatedTs(new Date().getTime());
        user.setPassword(password);
        user.setPassword(aesEncrypt(user));
    }
}
