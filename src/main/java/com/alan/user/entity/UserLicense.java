package com.alan.user.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserLicense {

  private String firstName;
  private String lastName;
  private String dob;
  private String mobileNumber;
  private String macId;
  private int serialId;
  private boolean isActive;
  private Date expiryDate;
  private Date issuedDate;
  private Date lastRenewed;

}
