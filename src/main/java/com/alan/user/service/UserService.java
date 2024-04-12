package com.alan.user.service;

import com.alan.user.Constants.UsersCode;
import com.alan.user.dao.UserRepository;
import com.alan.user.entity.Users;
import com.alan.user.exception.UsersException;
import com.alan.user.utils.AESUtils;
import com.alan.user.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

  @Autowired
  private UserRepository userServiceDao;

  @Autowired
  private UserUtils userUtils;

  @Autowired
  private AESUtils aesUtils;

  @Autowired
  private UserRepository userRepository;

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

  public List<Users> getAllUsers() {
    return userRepository.findAll();
  }

  public Users save(Users user) {
    try {
      return saveUsers(user);
    } catch (Exception e) {
      logger.error("Error occurred while add or update user details ", e);
      throw new UsersException("Error Occurred", e, UsersCode.INTERNAL_SERVER_ERROR);
    }
  }

  public Users update(Users user, String oldPassword, String newPassword) {
    logger.debug(user.toString());
    return updateUser(user, oldPassword, newPassword);
  }

  public void deleteUser(long id) {
    try {
      userServiceDao.deleteById(id);
    } catch (Exception e) {
      logger.error("Error occurred while deleting user ", e);
      throw new UsersException("User delete operation failed ", e, UsersCode.INTERNAL_SERVER_ERROR);
    }
  }

  private Users saveUsers(Users user) {
    setEncryptedPassword(user, base64DecodePassword(user.getPassword()));
    user = saveOrUpdateUser(user);
    user.setPassword("");
    return user;
  }

  private Users updateUser(Users user, String oldPassword, String newPassword) {
    System.out.println("OldPassword " + oldPassword);
    Users actualUser = userServiceDao.findById(user.getId()).get();
    actualUser.setPassword(aesDecrypt(actualUser));
    if (!checkPassword(actualUser, oldPassword)) {
      logger.info("Looks like Bad Request, Old password is not matching !!!");
      logger.info(" Actual Password " + actualUser.getPassword() + " -- Password " + oldPassword);
      throw new UsersException("Password Not matching", UsersCode.BAD_REQUEST);
    }
    setEncryptedPassword(actualUser, base64DecodePassword(newPassword));
    logger.debug("Updated user");
    actualUser = saveOrUpdateUser(actualUser);
    actualUser.setPassword("");
    return actualUser;
  }

  private boolean checkPassword(Users actualUser, String oldPassword) {
    String password = base64DecodePassword(oldPassword);
    return password.equals(actualUser.getPassword());
  }

  private Users saveOrUpdateUser(Users user) {
    return userServiceDao.save(user);
  }

  private String aesEncrypt(Users user) {
    return aesUtils.encrypt(user.getPassword().toCharArray(), user.getCreatedTs().format(formatter));
  }

  private String aesDecrypt(Users user) {
    return aesUtils.decrypt(user.getPassword().toCharArray(), user.getCreatedTs().format(formatter));
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
    user.setCreatedTs(LocalDateTime.now());
    user.setPassword(password);
    user.setPassword(aesEncrypt(user));
  }
}
