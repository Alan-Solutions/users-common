package com.alan.user.service;

import com.alan.user.entity.Users;
import com.alan.user.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserUtils userUtils;

  private Users testUser;

  @BeforeEach
  public void setUp() {
    testUser = new Users();
    testUser.setFirstName("Sujith");
    testUser.setLastName("Ramanathan");
    testUser.setCreatedTs(LocalDateTime.now());
    testUser.setRoles(Arrays.asList("write,read"));
    testUser.setMobileNo("9791590791");
    testUser.setStatus("active");
    testUser.setPassword(Base64.getEncoder().encodeToString("test".getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  @Order(1)
  public void saveUserTest() {
    Users createdUser = userService.save(testUser);
    assertEquals(createdUser.getMobileNo(), testUser.getMobileNo());
  }

  @Test
  @Order(2)
  public void getUsersTest() {
    List<Users> users = userService.getAllUsers();
    Users user = userService.getUser(users.get(0).getId());
    System.out.println("users.toString() " + user);
    assertEquals(user.getMobileNo(), testUser.getMobileNo());
  }

  @Test
  @Order(3)
  public void loginTest() {
    List<Users> users = userService.getAllUsers();
    Users user = userService.getUser(users.get(0).getId());
    assertEquals(user.getPassword(), new String(Base64.getDecoder().decode(testUser.getPassword())));
  }

  @Test
  @Order(4)
  public void updateUserTest() {
    List<Users> existingUsers = userService.getAllUsers();
    Users user = userService.getUser(existingUsers.get(0).getId());
    String oldPassword = user.getPassword(), tempPwd = userUtils.base64Encode("myTest");
    userService.update(user, userUtils.base64Encode(user.getPassword()), tempPwd);
    System.out.println("Old Password " + oldPassword);
    Users updatedUser = userService.getUser(user.getId());
    System.out.println(String.format("newPassword %s", updatedUser.getPassword()));
  }

  @Test
  @Order(5)
  public void delete() {
    List<Users> users = userService.getAllUsers();
    if (!users.isEmpty())
      userService.deleteUser(users.get(0).getId());
  }

  @Test
  @Order(6)
  public void deleteAll() {
    List<Users> users = userService.getAllUsers();
    if (!users.isEmpty()) {
      for (Users user : users) {
        userService.deleteUser(user.getId());
      }
    }
  }
}
