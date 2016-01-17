package com.usa.dev.drawerlayout.API;

/**
 * Author: Philippe Volpe
 * Date: 14/01/2016
 * Purpose: Generic objet to handle API errors
 */

public class APIError {

    String message;
    int errorCode;
    int errorSubCode;

    public APIError() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getErrorSubCode() {
        return errorSubCode;
    }

    public String getMessage() {
        return message;
    }

}
