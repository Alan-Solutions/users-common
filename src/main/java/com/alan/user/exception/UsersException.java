package com.alan.user.exception;

import com.alan.user.Constants.UsersCode;
import lombok.Data;

@Data
public class UsersException extends RuntimeException {

    private String message;
    private UsersCode usersCode;

    public UsersException(String message, Throwable cause, UsersCode usersCode) {
        super(message, cause);
        this.message = message;
        this.usersCode = usersCode;
    }

    public UsersException(String message, UsersCode usersCode) {
        super(message);
        this.message = message;
        this.usersCode = usersCode;
    }

}
