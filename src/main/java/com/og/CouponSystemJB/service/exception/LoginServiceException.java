package com.og.CouponSystemJB.service.exception;

/**
 * This will represent Exceptions that are related to the LoginService.
 */
public class LoginServiceException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /*---------------------- General ---------------------------*/

    public static final String EMAIL_NOT_FOUND = "Email is not registered ";

    public static final String PASS_NOT_CORRECT = "Password is incorrect ";

    public static final String CLIENT_NOT_FOUND = "Error finding corresponding Client ";

    public static final String INVALID_CLIENT = "Error finding corresponding Client ";


    public LoginServiceException(String msg){
        super(msg);
    }
}
