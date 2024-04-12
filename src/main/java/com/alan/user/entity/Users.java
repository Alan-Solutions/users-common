package com.alan.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString(exclude = {"password"})
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "role")
  private List<String> roles;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "created_ts", nullable = false, updatable = false)
  private LocalDateTime createdTs;

  @Column(name = "mobile_no", nullable = false)
  private String mobileNo;

}