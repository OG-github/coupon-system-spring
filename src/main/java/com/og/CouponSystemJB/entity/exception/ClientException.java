package com.og.CouponSystemJB.entity.exception;

/**
 * This will represent Exceptions that are related to the Client Mapped Super Class entity.
 */
public class ClientException extends Exception {

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

    public ClientException(String msg) {
        super("Client Exception " + msg);
    }
}
