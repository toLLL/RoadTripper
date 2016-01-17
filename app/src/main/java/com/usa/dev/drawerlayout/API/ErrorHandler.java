package com.usa.dev.drawerlayout.API;

import android.util.Log;

import com.facebook.AccessToken;
import com.usa.dev.drawerlayout.Helper.RequestManager;

import java.util.concurrent.Callable;

/**
 * Author: Philippe Volpe
 * Date: 14/01/2016
 * Purpose: Error Body handling
 */

public class ErrorHandler {

    APIError error;

    public ErrorHandler() {
    }

    public APIError getError() {
        return error;
    }

    public void process(int requestCode, Callable<?> callable) throws Exception {
        switch (requestCode) {
            case 400:
                if (this.getError().getErrorCode() == 1) {
                    // 400 - ErrorCode 1
                    this.invalidRequestError();
                }
                break;
            case 401:
                switch (this.getError().getErrorCode()) {
                    case 1:
                        switch (this.getError().getErrorSubCode()) {
                            case 1:
                                // 401 - ErrorCode 1 - errorSubCode 1
                                this.clientAuthRequired();
                                break;
                            case 2:
                                // 401 - ErrorCode 1 - errorSubCode 2
                                this.userAuthRequired();
                                break;
                        }
                        break;
                    case 2:
                        switch (this.getError().getErrorSubCode()) {
                            case 1:
                                // 401 - ErrorCode 2 - errorSubCode 1
                                this.expiredToken();
                                callable.call();
                                break;
                            case 2:
                                // 401 - ErrorCode 2 - errorSubCode 2
                                this.wrongToken();
                                callable.call();
                                break;
                        }
                        break;
                    case 3:
                        // 401 - ErrorCode 3
                        this.invalidFacebookToken();
                        callable.call();
                }
                break;
            case 404:
                if (this.getError().getErrorCode() == 1) {
                    // 404 - ErrorCode 1
                    this.notFoundError();
                }
                break;
            case 500:
                switch (this.getError().getErrorCode()) {
                    case 1:
                        // 500 - ErrorCode 1
                        this.internalServerError();
                        break;
                    case 2:
                        // 500 - ErrorCode 2
                        this.invalidFacebookReply();
                        break;
                }
        }
    }


    /**
     * Http code: 400
     * ErrorCode: 1
     * Purpose: Invalid request, check documentation for more details
     * Throw: RunTimeException (Should never reach here)
     */
    private void invalidRequestError() {
        Log.e("ErrorHandler", "invalidRequestError: Invalid Request");
        throw new RuntimeException("Invalid request: The application will close.");
    }

    /**
     * Http code: 401
     * ErrorCode: 1
     * ErrorSubCode: 1
     * Purpose: Request need to be authorized by client auth (check API documentation)
     * Throw: RunTimeException (Should never reach here)
     */
    private void clientAuthRequired() {
        Log.e("ErrorHandler", "clientAuthRequired: "+this.getError().getMessage());
        throw new RuntimeException(this.getError().getMessage());
    }

    /**
     * Http code: 401
     * ErrorCode: 1
     * ErrorSubCode: 2
     * Purpose: Request need to be authorized by user auth (Check API documentation)
     * Throw: RunTimeException (Should never reach here)
     */
    private void userAuthRequired() {
        Log.e("ErrorHandler", "userAuthRequired: "+this.getError().getMessage());
        throw new RuntimeException(this.getError().getMessage());
    }

    /**
     * Http code: 401
     * ErrorCode: 2
     * ErrorSubCode: 1
     * Purpose: Token expired, renew token
     */

    private void expiredToken() {
        Log.d("ErrorHandler", "expiredToken: Token expired, please renew");
        RequestManager.getInstance().renewToken();
    }

    /**
     * Http code: 401
     * ErrorCode: 2
     * ErrorSubCode: 2
     * Purpose: Invalid token, login again to obtain a new one
     */
    private void wrongToken() {
        Log.d("ErrorHandler", "wrongToken: Login again into the API server to obtain valid token");
        RequestManager.getInstance().login(AccessToken.getCurrentAccessToken().getToken());
    }

    /**
     * Http code: 401
     * ErrorCode: 3
     * Purpose: Invalid facebook token, updating.
     */
    private void invalidFacebookToken() {
        Log.d("ErrorHandler", "invalidFacebookToken: updating facebook token");
        RequestManager.getInstance().updateFacebookToken(AccessToken.getCurrentAccessToken().getToken());
    }

    /**
     * Http code : 404
     * ErrorCode: 1
     * Purpose: Not found
     * Throw: RunTimeException (Should never reach here)
     */
    private void notFoundError() {
        Log.e("ErrorHandler", "notFoundError: Not Found");
        throw new RuntimeException("Access error: 404 - Not Found");
    }

    /**
     * Http code: 500
     * ErrorCode: 1
     * Purpose: Internal server error
     * Throw: RunTimeException (Should never reach here)
     */
    private void internalServerError() {
        Log.e("ErrorHandler", "internalServerError: Server encountered problem");
        throw new RuntimeException("internalServerError: The application will close.");
    }

    /**
     * Http code: 500
     * ErrorCode: 2
     * Purpose: Internal server error (Facebook reply)
     * Throw: RunTimeException (Should never reach here)
     */
    private void invalidFacebookReply() {
        Log.e("ErrorHandler", "invalidFacebookReply: internal server error (FacebookReply)");
        throw new RuntimeException("Invalid Facebook Reply: The application will close");
    }
}