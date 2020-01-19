package com.og.CouponSystemJB.service.exception;

public class AdminServiceException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Exception message if a parameter is null.
     */
    public static String MSG_NULL_PARAM = "Null. ";

    /**
     * Exception message if invalid Company
     */
    public static String INVALID_COMP = "Invalid Company: ";

    /**
     * Exception message if invalid Customer
     */
    public static String INVALID_CUST = "Invalid Customer: ";

    public AdminServiceException(String msg) {
        super("AdminServiceException: " + msg);
    }

}