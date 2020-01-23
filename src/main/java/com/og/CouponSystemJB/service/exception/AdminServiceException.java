package com.og.CouponSystemJB.service.exception;

public class AdminServiceException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /*---------------------- General ---------------------------*/

    /**
     *
     */
    public static final String NULL_EMAIL = "null email ";

    /**
     *
     */
    public static final String NULL_PASSWORD = "null password ";

    /**
     *
     */
    public static final String EMAIL_SHORT = "email too short ";

    /**
     *
     */
    public static final String PASSWORD_SHORT = "password too short ";

    /**
     *
     */
    public static final String EMAIL_TAKEN = "Invalid: email already taken by other user.";

    /*---------------------- Company ---------------------------*/

    /**
     * Exception message if invalid Company.
     */
    public static final String INVALID_COMP = "Invalid Company: ";

    /**
     *
     */
    public static final String NULL_COMP = "null Company ";

    /**
     *
     */
    public static final String NULL_COMP_NAME = "null name ";

    /**
     *
     */
    public static final String COMP_NAME_SHORT = "Company name too short ";

    /**
     *
     */
    public static final String COMP_NAME_TAKEN = "name already taken ";

    /**
     *
     */
    public static final String COMP_NOT_FOUND = "Company not found";

    /*---------------------- Customer ---------------------------*/

    /**
     * Exception message if invalid Customer.
     */
    public static final String INVALID_CUST = "Invalid Customer: ";

    /**
     *
     */
    public static final String NULL_CUST = "null Customer ";

    /**
     *
     */
    public static final String NULL_FIRST_NAME = "null first name ";

    /**
     *
     */
    public static final String NULL_LAST_NAME = "null last name ";

    /**
     *
     */
    public static final String FIRST_NAME_SHORT = "first name too short ";

    /**
     *
     */
    public static final String LAST_NAME_SHORT = "last name too short ";

    /**
     *
     */
    public static final String CUST_NOT_FOUND = "Customer not found";

    public AdminServiceException(String msg) {
        super("AdminServiceException: " + msg);
    }

}