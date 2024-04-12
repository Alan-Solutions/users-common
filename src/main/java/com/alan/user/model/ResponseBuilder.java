package com.alan.user.model;

import com.alan.user.Constants.UsersCode;
import lombok.Data;

@Data
public class ResponseBuilder<T> {

    private boolean expOccured;
    private T actual;
    private UsersCode statusCode;

}
