package com.og.CouponSystemJB.service.exception;

/**
 * This will represent Exceptions that are related to the AdminServices.
 */
public class AdminServiceException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /*---------------------- General ---------------------------*/

    /**
     * Exception message for null email.
     */
    public static final String NULL_EMAIL = "null email ";

    /**
     * Exception message for null password.
     */
    public static final String NULL_PASSWORD = "null password ";

    /**
     * Exception message if email is too short.
     */
    public static final String EMAIL_SHORT = "email too short ";

    /**
     * Exception message if password is too short.
     */
    public static final String PASSWORD_SHORT = "password too short ";

    /**
     * Exception message for email taken by other user.
     */
    public static final String EMAIL_TAKEN = "Invalid: email already taken by other user.";

    /*---------------------- Company ---------------------------*/

    /**
     * Exception message if invalid Company.
     */
    public static final String INVALID_COMP = "Invalid Company: ";

    /**
     * Exception message if null Company.
     */
    public static final String NULL_COMP = "null Company ";

    /**
     * Exception message if null Company name.
     */
    public static final String NULL_COMP_NAME = "null name ";

    /**
     * Exception message if Company name is too short.
     */
    public static final String COMP_NAME_SHORT = "Company name too short ";

    /**
     * Exception message for Company name already taken by another Company.
     */
    public static final String COMP_NAME_TAKEN = "name already taken ";

    /**
     * Exception message for Company not found in the DB.
     */
    public static final String COMP_NOT_FOUND = "Company not found";

    /*---------------------- Customer ---------------------------*/

    /**
     * Exception message if invalid Customer.
     */
    public static final String INVALID_CUST = "Invalid Customer: ";

    /**
     * Exception message if null Customer.
     */
    public static final String NULL_CUST = "null Customer ";

    /**
     * Exception message if null Customer first name.
     */
    public static final String NULL_FIRST_NAME = "null first name ";

    /**
     * Exception message if null Customer last name.
     */
    public static final String NULL_LAST_NAME = "null last name ";

    /**
     * Exception message if Customer first name too short.
     */
    public static final String FIRST_NAME_SHORT = "first name too short ";

    /**
     * Exception message if Customer last name is too short.
     */
    public static final String LAST_NAME_SHORT = "last name too short ";

    /**
     * Exception message for Customer not found in the DB.
     */
    public static final String CUST_NOT_FOUND = "Customer not found";

    public AdminServiceException(String msg) {
        super("AdminServiceException: " + msg);
    }

}