package com.og.CouponSystemJB.service.exception;

/**
 * This will represent Exceptions that are related to the CustomerService.
 */
public class CustomerServiceException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /*---------------------- User ---------------------------*/

    /**
     * Exception message if User was not found in DB.
     */
    public static final String USER_NOT_FOUND = "Update User info failed. User was not found in DB ";

    /*---------------------- General ---------------------------*/

    /**
     * Exception message if invalid Customer.
     */
    public static final String INVALID_CUST = "Invalid Customer: ";

    /**
     * Exception message for updating Customer in DB failed.
     */
    public static final String UPDATE_CUST = "Update Customer fail: ";

    /**
     * Exception message if null Customer.
     */
    public static final String NULL_CUST = "null Customer ";

    /**
     * Exception message for null first name.
     */
    public static final String NULL_FIRST_NAME = "null first name ";

    /**
     * Exception message for null last name.
     */
    public static final String NULL_LAST_NAME = "null last name ";

    /**
     * Exception message if null email.
     */
    public static final String NULL_EMAIL = "null email ";

    /**
     * Exception message if null password.
     */
    public static final String NULL_PASSWORD = "null password ";

    /**
     * Exception message if trying to change Email.
     */
    public static final String EMAIL_CHANGE = "Can not change email ";

    /**
     * Exception message if password is too short.
     */
    public static final String PASSWORD_SHORT = "Password too short ";

    /**
     * Exception message if first name is too short.
     */
    public static final String FIRST_NAME_SHORT = "First name too short ";

    /**
     * Exception message if last name is too short.
     */
    public static final String LAST_NAME_SHORT = "Last name too short ";

    /*---------------------- Coupon ---------------------------*/

    /**
     * Exception message for purchasing Coupon in DB failed.
     */
    public static final String COUP_PURCH = "Purchase Coupon Failed: ";

    /**
     * Exception message for not finding a Coupon in DB.
     */
    public static final String COUP_NOT_EXIST = "Coupon does not exist ";

    public CustomerServiceException(String msg) {
        super(msg);
    }
}