package com.og.CouponSystemJB.entity.exception;

/**
 * This will represent Exceptions that are related to the User entity.
 */
public class UserException extends Exception {

    /*----------------- CONSTANTS ---------------------------------------------------------------------------------------*/

    /**
     * Exception message if a parameter is null.
     */
    public static String MSG_NULL_PARAM = "Null. ";

    /**
     * Exception message if a parameter is below minimun characters (MIN_CHAR).
     */
    public static String MSG_MIN_PARAM = "Below minimum characters. ";

    /**
     * Exception message if email is invalid.
     */
    public static String MSG_EMAIL = "Invalid email: ";

    /**
     * Exception message if password is invalid.
     */
    public static String MSG_PASSWORD = "Invalid password: ";

    /**
     * Exception message if Client is invalid.
     */
    public static String MSG_Client = "Invalid Client: ";


    public UserException(String msg){
        super("User Exception " + msg);
    }
}
