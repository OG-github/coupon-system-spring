package com.og.CouponSystemJB.entity.exception;

/**
 * This will represent Exceptions that are related to the Customer entity.
 */
public class CustomerException extends Exception {

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
     * Exception message if first name is invalid.
     */
    public static String MSG_FIRST_NAME = "Invalid first name: ";

    /**
     * Exception message if last name is invalid.
     */
    public static String MSG_LAST_NAME = "Invalid last name: ";

    /**
     * Exception message if email is invalid.
     */
    public static String MSG_EMAIL = "Invalid email: ";

    /**
     * Exception message if password is invalid.
     */
    public static String MSG_PASSWORD = "Invalid password: ";

    /**
     * Exception message if CustomerCoupons are invalid.
     */
    public static String MSG_COUPONS = "Invalid CustomerCoupons: ";

    public CustomerException(String msg){
        super(msg);
    }

}
