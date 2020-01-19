package com.og.CouponSystemJB.entity.exception;

/**
 * This will represent Exceptions that are related to the Company entity.
 */
public class CompanyException extends ClientException {

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
     * Exception message if name is invalid.
     */
    public static String MSG_NAME = "Invalid name: ";

    /**
     * Exception message if email is invalid.
     */
    public static String MSG_EMAIL = "Invalid email: ";

    /**
     * Exception message if password is invalid.
     */
    public static String MSG_PASSWORD = "Invalid password: ";

    /**
     * Exception message if Coupons Collection is invalid.
     */
    public static String MSG_COUPONS = "Invalid Coupon Collection: ";

    /**
     * Exception message if Coupon is invalid.
     */
    public static String MSG_COUPON = "Invalid Coupon: ";

    public CompanyException(String msg) {
        super("Company Exception " + msg);
    }
}
